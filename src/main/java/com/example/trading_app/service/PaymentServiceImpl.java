package com.example.trading_app.service;
import com.example.trading_app.Entity.PaymentOrder;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.PaymentMethod;
import com.example.trading_app.domain.PaymentOrderStatus;
import com.example.trading_app.repository.PaymentOrderRepository;
import com.example.trading_app.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;
    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Value("${razorpay.api.key}")
    private String razorPayApiKey;
    @Value("${stripe.api.secret}")
    private String razorPaySecretApiKey;
    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {
        return paymentOrderRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Payment Order Not Found"));
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(razorPayApiKey,razorPaySecretApiKey);
                Payment payment =razorpayClient.payments.fetch(paymentId);
                Integer amount =payment.get("amount");
                String status=payment.get("status");
                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;

            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return  true;
        }
        return null;
    }

    @Override
    public PaymentResponse createRazorPayPaymentUrl(User user, Long amount) {
        Long Amount =amount*100;
        try{
            RazorpayClient razorPay= new RazorpayClient(razorPayApiKey,razorPaySecretApiKey);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");
            //set JSON Object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());
            paymentLinkRequest.put("customer",customer);

            //Create a JSON object with the Notification settings
            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            //set the remainder settings
            paymentLinkRequest.put("remainder_enable",true);

            //set the callback url and method

            paymentLinkRequest.put("callback_url","http://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink = razorPay.paymentLink.create( paymentLinkRequest);
            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl =paymentLink.get("short_url");

            PaymentResponse response=new PaymentResponse();
            response.setPaymentUrl(paymentLinkUrl);
            return response;


        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentResponse createStripePaymentUrl(User user, Long amount, String orderId) {
        return null;
    }
}
