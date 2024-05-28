import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CompteCRUDTest {

    @Mock
    private CompteRepository compteRepository;

    @InjectMocks
    private CompteService compteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreerCompte() {
        // Données de test
        Compte compte = new Compte("123456789", 1000.0);

        // Comportement simulé du repository lors de la création du compte
        when(compteRepository.save(compte)).thenReturn(compte);

        // Appel de la méthode à tester
        Compte result = compteService.creerCompte(compte);

        // Vérification du résultat
        assertEquals(compte, result);

        // Vérification que la méthode du repository a été appelée avec les bons arguments
        verify(compteRepository, times(1)).save(compte);
    }

    @Test
    public void testCrediterCompte() {
        // Données de test
        int compteId = 1;
        double montant = 500.0;
        Compte compte = new Compte("123456789", 1000.0);

        // Comportement simulé du repository lors de la récupération et mise à jour du compte
        when(compteRepository.findById(compteId)).thenReturn(Optional.of(compte));
        when(compteRepository.save(compte)).thenReturn(compte);

        // Appel de la méthode à tester
        boolean result = compteService.crediterCompte(compteId, montant);

        // Vérification du résultat
        assertEquals(true, result);
        assertEquals(1500.0, compte.getSolde());

        // Vérification que les méthodes du repository ont été appelées avec les bons arguments
        verify(compteRepository, times(1)).findById(compteId);
        verify(compteRepository, times(1)).save(compte);
    }

    @Test
    public void testDebiterCompte() {
        // Données de test
        int compteId = 1;
        double montant = 200.0;
        Compte compte = new Compte("123456789", 1000.0);

        // Comportement simulé du repository lors de la récupération et mise à jour du compte
        when(compteRepository.findById(compteId)).thenReturn(Optional.of(compte));
        when(compteRepository.save(compte)).thenReturn(compte);

        // Appel de la méthode à tester
        boolean result = compteService.debiterCompte(compteId, montant);

        // Vérification du résultat
        assertEquals(true, result);
        assertEquals(800.0, compte.getSolde());

        // Vérification que les méthodes du repository ont été appelées avec les bons arguments
        verify(compteRepository, times(1)).findById(compteId);
        verify(compteRepository, times(1)).save(compte);
    }

    @Test
    public void testEffectuerVirement() {
        // Données de test
        int compteIdDebiteur = 1;
        int compteIdCrediteur = 2;
        double montant = 200.0;
        Compte compteDebiteur = new Compte("123456789", 1000.0);
        Compte compteCrediteur = new Compte("987654321", 500.0);

        // Comportement simulé du repository lors de la récupération et mise à jour des comptes
        when(compteRepository.findById(compteIdDebiteur)).thenReturn(Optional.of(compteDebiteur));
        when(compteRepository.findById(compteIdCrediteur)).thenReturn(Optional.of(compteCrediteur));
        when(compteRepository.save(compteDebiteur)).thenReturn(compteDebiteur);
        when(compteRepository.save(compteCrediteur)).thenReturn(compteCrediteur);

        // Appel de la méthode à tester
        boolean result = compteService.effectuerVirement(compteIdDebiteur, compteIdCrediteur, montant);

        // Vérification du résultat
        assertEquals(true, result);
        assertEquals(800.0, compteDebiteur.getSolde());
        assertEquals(700.0, compteCrediteur.getSolde());

        // Vérification que les méthodes du repository ont été appelées avec les bons arguments
        verify(compteRepository, times(1)).findById(compteIdDebiteur);
        verify(compteRepository, times(1)).findById(compteIdCrediteur);
        verify(compteRepository, times(1)).save(compteDebiteur);
        verify(compteRepository, times(1)).save(compteCrediteur);
    }

    @Test
    public void testCloturerCompte() {
        // Données de test
        int compteId = 1;
        Compte compte = new Compte("123456789", 1000.0);

        // Comportement simulé du repository lors de la récupération et suppression du compte
        when(compteRepository.findById(compteId)).thenReturn(Optional.of(compte));
        doNothing().when(compteRepository).deleteById(compteId);

        // Appel de la méthode à tester
        boolean result = compteService.cloturerCompte(compteId);

        // Vérification du résultat
        assertEquals(true, result);

        // Vérification que les méthodes du repository ont été appelées avec les bons arguments
        verify(compteRepository, times(1)).findById(compteId);
        verify(compteRepository, times(1)).deleteById(compteId);
    }
}
