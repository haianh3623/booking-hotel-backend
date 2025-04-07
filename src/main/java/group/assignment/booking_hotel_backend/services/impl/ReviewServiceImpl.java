package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.Review;
import group.assignment.booking_hotel_backend.repository.ReviewRepository;
import group.assignment.booking_hotel_backend.services.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void save(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Review findById(Integer id) {
        Optional<Review> result = reviewRepository.findById(id);
        Review review  = null;
        if(result.isPresent()){
            review = result.get();
        }
        else{
            throw new RuntimeException("Không thấy review có id là: " + id);
        }
        return review;
    }

    @Override
    public void deleteById(Integer id) {
        reviewRepository.deleteById(id);
    }
}