package com.ms.reviewms.review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews(Long companyId);
    boolean addReview(Long companyId, Review review);
    Review getReview(Long reviewId);
    boolean updatedReview(Long reviewId, Review updatedReview);
    boolean deleteReview(Long reviewId);
}
