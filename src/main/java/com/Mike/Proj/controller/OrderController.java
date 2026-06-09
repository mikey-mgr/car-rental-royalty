// package com.Mike.Proj.controller;

// import java.io.DataOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.net.URL;
// import java.util.List;

// import javax.net.ssl.HttpsURLConnection;

// import org.apache.tomcat.util.http.fileupload.IOUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.Mike.Proj.dto.checkout.CheckoutItemDto;
// import com.Mike.Proj.service.AuthenticationService;
// import com.Mike.Proj.service.OrderService;

// @RestController
// @RequestMapping("/order")
// public class OrderController {

//     // Checkout session API
//     private String request() throws IOException {
//         @SuppressWarnings("deprecation")
//         URL url = new URL("https://eu-test.oppwa.com/v1/checkouts");

//         HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//         conn.setRequestMethod("POST");
//         conn.setRequestProperty("Authorization", "Bearer [REDACTED]");
//         conn.setDoInput(true);
//         conn.setDoOutput(true);

//         String data = ""
//             + "entityId=8a8294174b7ecb28014b9699220015ca"
//             + "&amount=92.00"
//             + "&currency=USD"
//             + "&paymentType=DB";

//         DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//         wr.writeBytes(data);
//         wr.flush();
//         wr.close();
//         int responseCode = conn.getResponseCode();
//         InputStream is;

//         if (responseCode >= 400) {
//             is = conn.getErrorStream();
//         } else {
//             is = conn.getInputStream();
//         }

//         return IOUtils.toString(is, StandardCharsets.UTF_8);
//     }
// }
