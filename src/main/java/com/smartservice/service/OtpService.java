package com.smartservice.service;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service public class OtpService {
 private final SecureRandom random=new SecureRandom(); private final Map<String,Entry> store=new ConcurrentHashMap<>();
 public String generate(String email){String otp=String.format("%06d",random.nextInt(1000000));store.put(email.toLowerCase(),new Entry(otp,Instant.now().plusSeconds(300)));return otp;}
 public boolean verify(String email,String otp){Entry e=store.get(email.toLowerCase()); if(e==null||Instant.now().isAfter(e.expiry)||!e.otp.equals(otp))return false; store.remove(email.toLowerCase()); return true;}
 record Entry(String otp,Instant expiry){}
}
