package com.example.orderMovie.repository.jpa;

import com.example.orderMovie.domain.jpa.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, JpaSpecificationExecutor<Order>, PagingAndSortingRepository<Order, Long> {


}
