package com.parcezza.backend.service;

import com.parcezza.backend.dto.returnrequest.ReturnRequestCreate;
import com.parcezza.backend.dto.returnrequest.ReturnResponse;
import com.parcezza.backend.dto.returnrequest.ReturnStatusUpdateRequest;
import java.util.List;

public interface ReturnService {
    ReturnResponse requestReturn(Long orderId, ReturnRequestCreate request);
    ReturnResponse getByOrder(Long orderId);
    ReturnResponse updateStatus(Long returnId, ReturnStatusUpdateRequest request);
    List<ReturnResponse> listAll();
}
