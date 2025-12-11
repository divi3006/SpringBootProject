package com.example.wholesalehub.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.wholesalehub.Dto.AddressRequest;
import com.example.wholesalehub.Dto.AddressResponse;
import com.example.wholesalehub.Dto.ChangePasswordRequest;
import com.example.wholesalehub.Dto.UserprofileRequest;
import com.example.wholesalehub.Dto.UserprofileResponse;
import com.example.wholesalehub.Entity.Address;
import com.example.wholesalehub.Entity.Userprofile;
import com.example.wholesalehub.Entity.Users;
import com.example.wholesalehub.Repository.AddressRepository;
import com.example.wholesalehub.Repository.UserRepository;
import com.example.wholesalehub.Repository.UserprofileRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Service
public class UserService {
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private UserprofileRepository userprofilerepository;
	
	@Autowired
	private AddressRepository addressrepository;
	
	@Autowired
	private PasswordEncoder passwordencoder;
	
	private static final Logger logger=LoggerFactory.getLogger(UserService.class);

	//create userprofile
	public Userprofile createprofile(int userid, UserprofileRequest userprofilerequest) {
		Users user=userrepository.findById(userid).orElseThrow(()->new IllegalArgumentException("Invalid userid:"+userid));
		
		Address shippingaddress=new Address();
		if(userprofilerequest.getShippingAddress()!=null) {
			shippingaddress.setStreet(userprofilerequest.getShippingAddress().getStreet());
			shippingaddress.setCity(userprofilerequest.getShippingAddress().getCity());
			shippingaddress.setState(userprofilerequest.getShippingAddress().getState());
			shippingaddress.setPincode(userprofilerequest.getShippingAddress().getPincode());			
		}
		
		Address billingaddress=new Address();
		if(userprofilerequest.getBillingAddress()!=null) {
			billingaddress.setStreet(userprofilerequest.getBillingAddress().getStreet());
			billingaddress.setCity(userprofilerequest.getBillingAddress().getCity());
			billingaddress.setState(userprofilerequest.getBillingAddress().getState());
			billingaddress.setPincode(userprofilerequest.getBillingAddress().getPincode());
		
	}
		
		Userprofile userprofile=new Userprofile();
		userprofile.setFirstname(userprofilerequest.getFirstname());
		userprofile.setLastname(userprofilerequest.getLastname());
		userprofile.setEmail(user.getEmail());
		userprofile.setPhonenumber(userprofilerequest.getPhonenumber());
		userprofile.setProfilepic(userprofilerequest.getProfilepic());
		userprofile.setUser(user);
		userprofile.setShippingAddress(shippingaddress);
		userprofile.setBillingAddress(billingaddress);
		
		userprofilerepository.save(userprofile);
        logger.info("Created UserProfile with shipping and billing addresses for userId {}", userid);
       return userprofile;
	}
	
	//Getuserprofile

	public UserprofileResponse getUserProfile(int userid) {
		Users user = userrepository.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));	
		
		Userprofile userprofile=user.getUserprofile();
		UserprofileResponse response = new UserprofileResponse();
	    response.setFirstname(userprofile.getFirstname());
	    response.setLastname(userprofile.getLastname());
	    response.setEmail(userprofile.getEmail());
	    response.setPhonenumber(userprofile.getPhonenumber());
	    response.setProfilepic(userprofile.getProfilepic());

	    // Map billing address
	    Address billing = userprofile.getBillingAddress();
	    if (billing != null) {
	        AddressResponse billingResp = new AddressResponse();
	        billingResp.setStreet(billing.getStreet());
	        billingResp.setCity(billing.getCity());
	        billingResp.setState(billing.getState());
	        billingResp.setPincode(billing.getPincode());
	        response.setBillingAddress(billingResp);
	    }

	    // Map shipping address
	    Address shipping = userprofile.getShippingAddress();
	    if (shipping != null) {
	        AddressResponse shippingResp = new AddressResponse();
	        shippingResp.setStreet(shipping.getStreet());
	        shippingResp.setCity(shipping.getCity());
	        shippingResp.setState(shipping.getState());
	        shippingResp.setPincode(shipping.getPincode());
	        response.setShippingAddress(shippingResp);
	    }

	    return response;
		}


	//update profilepic
	public String updateProfilePic(int userId, MultipartFile file) throws Exception{
		
	    // Fetch the user from the database
        Users user = userrepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //  Delete old profile picture if exists
        String oldFilePath = user.getUserprofile().getProfilepic();
        if (oldFilePath != null && !oldFilePath.isEmpty()) {
            Path oldPath = Paths.get(oldFilePath);
            try {
                Files.deleteIfExists(oldPath);
                logger.info("Old profile picture deleted: {}", oldFilePath);
            } catch (Exception e) {
                logger.warn("Failed to delete old profile picture: {}", oldFilePath);
            }
        }

        // Save the new profile picture
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/profilePics/" + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        logger.info("New profile picture uploaded at: {}", path.toAbsolutePath());

        // Update database with new profile picture path
        user.getUserprofile().setProfilepic(path.toString());
        userrepository.save(user);
        logger.info("User profile database updated with new profile picture");

        // Return the new file path
        return path.toString();
	}
//change password
	public void changePassword(int userid, ChangePasswordRequest request) {
		  Users user = userrepository.findById(userid)
	                .orElseThrow(() -> new IllegalArgumentException("User not found"));

		  if (!passwordencoder.matches(request.getOldPassword(), user.getPassword())) {
	            throw new IllegalArgumentException("Old password is incorrect");
	        }
	        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
	            throw new IllegalArgumentException("New password and confirm password do not match");
	        }

	        user.setPassword(passwordencoder.encode(request.getNewPassword()));
	        userrepository.save(user);
	    }
	//updating shipping address

		public void updateShippingAddress(int userid, AddressRequest request) {
	        Userprofile userprofile = userprofilerepository.findById(userid)
	                .orElseThrow(() -> new IllegalArgumentException("User not found"));

	        Address address = userprofile.getShippingAddress();
	        if (address == null) {
	            address = new Address();
	        }

	        address.setStreet(request.getStreet());
	        address.setCity(request.getCity());
	        address.setState(request.getState());
	        address.setPincode(request.getPincode());

	        addressrepository.save(address);
	        userprofile.setShippingAddress(address);
	        userprofilerepository.save(userprofile);
	    }
	 
	}
