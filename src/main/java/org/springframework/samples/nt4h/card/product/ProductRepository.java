package org.springframework.samples.nt4h.card.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findById(int id);

    List<Product> findAll();


}
