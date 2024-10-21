package dss.chatappv1.service;

import dss.chatappv1.model.AdUser;
import dss.chatappv1.model.CMCall;
import dss.chatappv1.model.CMCustomer;
import dss.chatappv1.model.HREmployee;
import dss.chatappv1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CMCallService {

    @Autowired
    CMCallRepository cmCallRepository;

    @Autowired
    AdClientRepository adClientRepository;
    @Autowired
    AdUserRepository adUserRepository;

    @Autowired
    AdOrgRepository adOrgRepository;

    @Autowired
    CMCustomerRepository cmCustomerRepository;

    @Autowired
    HREpmloyeeRepository hrEpmloyeeRepository;

    @Transactional
    public CMCall saveCMCall(CMCall request) {
        synchronized (this){
            request.setCmCallId(getUniqueCmCallId(request.getCmCallId()));
        }
        String customerName = null;
        String callerNumber = request.getCallerNumber();

        if (callerNumber.length() >= 10) {
            Optional<CMCustomer> customer = cmCustomerRepository.findByPhone(callerNumber);
            customerName = customer.map(CMCustomer::getName).orElse("Unknown Customer");
        }else {
            Optional<HREmployee> employee = hrEpmloyeeRepository.findByExtension(callerNumber);
            customerName = employee.map(HREmployee::getName).orElse("Unknown Employee");
        }


//        Optional<AdUser> adUser = adUserRepository.findByName(request.getUserName());
//        if (adUser.isPresent()) {
//            request.setAdUserId(adUser.get().getAdUserId());
//        } else {
//            throw new RuntimeException("User not found: " + request.getUserName());
//        }

        Optional<AdUser> destinationUser = adUserRepository.findByExtension(request.getDestinationNumber());
        if (destinationUser.isPresent()) {
            request.setAdUserId(destinationUser.get().getAdUserId());
            request.setAdClientId(destinationUser.get().getAdClientId());
            request.setAdOrgId(destinationUser.get().getAdOrgId());
            request.setCreatedBy(destinationUser.get().getCreatedBy());
            request.setUpdatedBy(destinationUser.get().getUpdatedBy());
        } else {
            throw new RuntimeException("Destination user not found for number: " + request.getDestinationNumber());
        }

        request.setCallId(request.getCallId());
        request.setCallStatus(request.getCallStatus());
        request.setDirection(request.getDirection());
        request.setCallerNumber(request.getCallerNumber());
        request.setDestinationNumber(request.getDestinationNumber());
        request.setStartTime(request.getStartTime());
        request.setAnswerTime(request.getAnswerTime());
        request.setHangupBy(request.getHangupBy());
        request.setRecordingUrl(request.getRecordingUrl());
        request.setTransactionId(request.getTransactionId());
        request.setObjectId(request.getObjectId());
        request.setUserName(request.getUserName());
        request.setEventType(request.getEventType());
        request.setCustomerName(customerName);

        return cmCallRepository.save(request);
    }
    public Optional<CMCall> findById(BigDecimal cmCallId) {
        return cmCallRepository.findByCmCallId(cmCallId);
    }

    public List<CMCall> getAllCalls() {
        return cmCallRepository.findAll();
    }

    public BigDecimal getUniqueCmCallId(BigDecimal initialId) {
        Optional<CMCall> existingCall = findById(initialId);
        BigDecimal uniqueId = initialId;

        // Tăng giá trị khóa chính cho đến khi không trùng lặp
        while (existingCall.isPresent()) {
            uniqueId = uniqueId.add(BigDecimal.ONE); // Tăng lên 1
            existingCall = findById(uniqueId);
        }

        return uniqueId;
    }


}
