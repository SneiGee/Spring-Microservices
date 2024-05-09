package com.ms.jobms.job.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ms.jobms.job.external.Review;

@FeignClient(name = "REVIEW-SERVICE", url = "${review-service.url}")
public interface ReviewClient {
    @GetMapping("/reviews")
    List<Review> getReview(@RequestParam("companyId") Long companyId);
}
    