import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        // Identifiants valides
        String username = "admin";
        String password = "1234";

        // Création d’un scanner pour lire les entrées utilisateur
        Scanner input = new Scanner(System.in);

        System.out.print("Enter le nom d'utilisateur : ");
        String userInput = input.nextLine();

        System.out.print("Enter le mot de passe : ");
        String passInput = input.nextLine();

        // Vérification
        if (userInput.equals(username) && passInput.equals(password)) {
            System.out.println("✅ Connexion réussie !");
        } else {
            System.out.println("❌ Nom d'utilisateur ou mot de passe incorrect.");

            input.close();
        }
    }

}