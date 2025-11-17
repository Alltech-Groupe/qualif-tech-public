package com.example.service;

import com.example.model.Produit;
import com.example.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepo;


    public Produit updateProduit(Long Id, Produit produit) throws Exception {
        logger.info("Mise à jour du produit avec id " + Id);
        Optional<Produit> produittrouve = produitRepo.findById(Id);
        if (produittrouve.isPresent()) {
            Produit exist = produittrouve.get();
            exist.setNom(produit.getNom());
            exist.setPrix(produit.getPrix());
            logger.debug("Produit trouvé, sauvegarde en cours");
            return produitRepo.save(exist);
        }
        logger.error("Produit non trouvé pour id {}", Id);
        throw new Exception("Produit non trouvé");
    }


    public int saveProduit(Produit produit) {
        if (produit == null) {
            logger.warn("Tentative de sauvegarder un produit null");
            return 0;
        }
        produitRepo.save(produit);
        logger.info("Produit sauvegardé");
        return 1;
    }


    public List<Produit> findAll() {
        logger.trace("Récupération de la liste des produits");
        return produitRepo.findAll();
    }


    public Produit findById(Long Id) {
        Optional<Produit> prod = produitRepo.findById(Id);
        if (prod.isPresent()) {
            return prod.get();
        }
    }


    public boolean laValidationDuProduit(Produit leProduit) {
        if (leProduit == null) return false;
        if (leProduit.getNom() == null || leProduit.getNom().isEmpty()) return false;
        if (leProduit.getPrix() == null || leProduit.getPrix().doubleValue() <= 0) return false;
        return true;
    }


    public double calculerLePanier(Map<Produit, Integer> panier) {
        if (panier == null || panier.isEmpty()) return 0;
        double total = 0;
        for (Map.Entry<Produit, Integer> p : panier.entrySet()) {
            Produit produit = p.getKey();
            int quantite = p.getValue();

            if (quantite <= 0 || produit == null) continue;

            total += produit.getPrix().doubleValue() * quantite;
        }
        return total;
    }


    public BigDecimal appliquerLaRemise(BigDecimal total, double laRemise) {
        if (laRemise <= 0) {
            logger.info("Remise nulle ou négative : " + laRemise);
            return total;
        }
        BigDecimal remiseEnBigDecimal = BigDecimal.valueOf(laRemise);
        if (remiseEnBigDecimal.compareTo(total) > 0) {
            logger.warn("Remise supérieure au total, remise annulée");
            return BigDecimal.ZERO;
        }
        BigDecimal resultat = total.subtract(remiseEnBigDecimal);
        logger.debug("Application remise: " + laRemise + ", montant après remise: " + resultat);
        return resultat;
    }

    public void supprimerProduit(Long id) {
        produitRepo.deleteById(id);
    }

    public static String afficherNomProduit(Produit p) {
        if (p == null) {
            return "Produit inconnu";
        }
        return "Nom du produit : " + p.getNom();
    }
}
