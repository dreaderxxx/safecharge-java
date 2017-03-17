package com.safecharge.retail.test.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.safecharge.retail.biz.SafechargeConfiguration;
import com.safecharge.retail.biz.SafechargeHttpClient;
import com.safecharge.retail.model.MerchantInfo;
import com.safecharge.retail.request.GetOrderDetailsRequest;
import com.safecharge.retail.request.GetSessionTokenRequest;
import com.safecharge.retail.request.OpenOrderRequest;
import com.safecharge.retail.request.PaymentAPMRequest;
import com.safecharge.retail.request.SafechargeRequest;
import com.safecharge.retail.request.UpdateOrderRequest;
import com.safecharge.retail.response.OpenOrderResponse;
import com.safecharge.retail.response.SafechargeResponse;
import com.safecharge.retail.response.UpdateOrderResponse;
import com.safecharge.retail.test.BaseTest;
import com.safecharge.retail.util.Constants;

/**
 * Copyright (C) 2007-2017 SafeCharge International Group Limited.
 *
 * @author <a mailto:nikolad@safecharge.com>Nikola Dichev</a>
 * @since 2/16/2017
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) public class APMWorkflowTest extends BaseTest {

    // All static field values will be shared between tests
    private static String sessionToken;
    private static String orderId;
    private static MerchantInfo merchantInfo;

    @Before public void init() {
        super.init();
        merchantInfo = new MerchantInfo("2QMy87kirFbtdkl6Ubk9xCqhNICYNCewiOCm19DhJp3lqAI6lp7Oh2rZsn61LVw9", "2885023999185468261", "5612",
                Constants.HashAlgorithm.SHA256);

        SafechargeConfiguration.init("http://dummy:1234/ppp/", SafechargeHttpClient.createDefault());
    }

    @Test public void test1_getSessionTokenTest() throws IOException {
        SafechargeRequest safechargeRequest = GetSessionTokenRequest.builder()
                                                                    .addMerchantInfo(merchantInfo)
                                                                    .build();
        SafechargeResponse response = safechargeRequestExecutor.executeRequest(safechargeRequest);

        Assert.assertTrue(response != null);
        Assert.assertTrue(Constants.APIResponseStatus.SUCCESS.equals(response.getStatus()));
        sessionToken = response.getSessionToken();
    }

    @Test public void test2_openOrder() {
        SafechargeRequest openOrderRequest = OpenOrderRequest.builder()
                                                             .addMerchantInfo(merchantInfo)
                                                             .addCurrency("EUR")
                                                             .addAmount("2")
                                                             .addSessionToken(sessionToken)
                                                             .addItem("test_item_1", "1", "1")
                                                             .addItem("test_item_2", "1", "1")
                                                             .addUserDetails("Test street 1", "Sofia", "BG", "test@test.com", "Test", "Testov",
                                                                     "0884123456", null, "1000")
                                                             .addBillingDetails("Test", "Testov", "test@test.com", "0884123456", "Test street 1",
                                                                     "Sofia", "BG", null, "1000", "0884123456")
                                                             .addShippingDetails("Test", "Testov", "test@test.com", "0884123456", "Test street 1",
                                                                     "Sofia", "BG", null, "1000", "0884123456")
                                                             .build();
        OpenOrderResponse openOrderResponse = (OpenOrderResponse) safechargeRequestExecutor.executeRequest(openOrderRequest);

        Assert.assertTrue(openOrderResponse != null);
        Assert.assertTrue(Constants.APIResponseStatus.SUCCESS.equals(openOrderResponse.getStatus()));

        orderId = openOrderResponse.getOrderId();
        Assert.assertTrue(orderId != null && !orderId.isEmpty());
    }

    @Test public void test3_updateOrder() {
        SafechargeRequest updateOrderRequest = UpdateOrderRequest.builder()
                                                                 .addMerchantInfo(merchantInfo)
                                                                 .addCurrency("EUR")
                                                                 .addAmount("2")
                                                                 .addSessionToken(sessionToken)
                                                                 .addItem("test_item_1", "1", "1")
                                                                 .addItem("test_item_2", "1", "1")
                                                                 .addUserDetails("Test street 1", "Sofia", "BG", "test@test.com", "Test", "Testov",
                                                                         "0884123456", null, "1000")
                                                                 .addBillingDetails("Test", "Testov", "test@test.com", "0884123456", "Test street 1",
                                                                         "Sofia", "BG", null, "1000", "0884123456")
                                                                 .addShippingDetails("Test", "Testov", "test@test.com", "0884123456", "Test street 1",
                                                                         "Sofia", "BG", null, "1000", "0884123456")
                                                                 .addOrderId(orderId)
                                                                 .build();

        UpdateOrderResponse updateOrderResponse = (UpdateOrderResponse) safechargeRequestExecutor.executeRequest(updateOrderRequest);

        Assert.assertTrue(updateOrderResponse != null);
        Assert.assertTrue(Constants.APIResponseStatus.SUCCESS.equals(updateOrderResponse.getStatus()));
    }

    @Test public void test4_getOrderDetails() {
        SafechargeRequest safechargeRequest = GetOrderDetailsRequest.builder()
                                                                    .addMerchantInfo(merchantInfo)
                                                                    .addOrderId(orderId)
                                                                    .addSessionToken(sessionToken)
                                                                    .build();
        SafechargeResponse response = safechargeRequestExecutor.executeRequest(safechargeRequest);

        Assert.assertTrue(response != null);
        Assert.assertTrue(Constants.APIResponseStatus.SUCCESS.equals(response.getStatus()));
    }

    @Test public void test5_paymentAPM() {
        Map<String, String> userAccountDetails = new HashMap<>();
        userAccountDetails.put("email", "nikolad_safecharge_2@abv.bg");
        userAccountDetails.put("account_id", "XX362V4DC76VU");
        SafechargeRequest request = PaymentAPMRequest.builder()
                                                     .addMerchantInfo(merchantInfo)
                                                     .addCurrency("EUR")
                                                     .addAmount("2")
                                                     .addSessionToken(sessionToken)
                                                     .addItem("test_item_1", "1", "1")
                                                     .addItem("test_item_2", "1", "1")
                                                     .addUserDetails("Test street 1", "Sofia", "BG", "test@test.com", "Test", "Testov", "0884123456",
                                                             null, "1000")
                                                     .addBillingDetails("Test", "Testov", "test@test.com", "0884123456", "Test street 1", "Sofia",
                                                             "BG", null, "1000", "0884123456")
                                                     .addShippingDetails("Test", "Testov", "test@test.com", "0884123456", "Test street 1", "Sofia",
                                                             "BG", null, "1000", "0884123456")
                                                     .addOrderId(orderId)
                                                     .addCountry("US")
                                                     .addURLDetails("https://apmtest.gate2shop.com/nikolappp/cashier/cancel.do",
                                                             "https://apmtest.gate2shop.com/nikolappp/defaultPending.do",
                                                             "https://apmtest.gate2shop.com/nikolappp/defaultSuccess.do")
                                                     .addPaymentMethod("apmgw_expresscheckout")
                                                     .addUserAccountDetails(userAccountDetails)
                                                     .build();
        SafechargeResponse response = safechargeRequestExecutor.executeRequest(request);

        Assert.assertTrue(response != null);
        Assert.assertTrue(Constants.APIResponseStatus.SUCCESS.equals(response.getStatus()));

    }

}