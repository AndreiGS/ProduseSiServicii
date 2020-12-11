package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Comments;
import com.findthebusiness.backend.entity.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, String> {

    Optional<List<Comments>> findCommentsByShop(Shops shop);
    Boolean existsByShopAndEmail(Shops shop, byte[] email);

}
