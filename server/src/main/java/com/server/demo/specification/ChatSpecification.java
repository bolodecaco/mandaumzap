package com.server.demo.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.server.demo.models.Chat;

import jakarta.persistence.criteria.Predicate;

public class ChatSpecification {
    
    public static Specification<Chat> withFilters(String userId, String search, UUID sessionId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filtro por userId (obrigatório)
            predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            
            // Filtro por texto de busca
            if (search != null && !search.trim().isEmpty()) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("chatName")), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("whatsAppId")), "%" + search.toLowerCase() + "%")
                ));
            }
            
            // Filtro por sessionId
            if (sessionId != null) {
                predicates.add(criteriaBuilder.equal(root.get("session").get("id"), sessionId));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}