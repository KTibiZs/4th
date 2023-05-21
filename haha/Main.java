package hu.ak_akademia.tributetocodebreakers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    private static final String basicLatinLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // from A to Z
    private static final String numerals = "0123456789"; // from 0 to 9
    private static final String ACCENTED_CHARS = "ÁÉÍÓÖŐÚÜŰ"; // Hungarian accented letters
    private static final String PUNCTUATIONS = " ,.;:+-%"; // punctuations

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {

            String messageToCipher = "";
            String textContent = "";
            String fileName = "Heureka";
            String answer = "";

            int shift;
            int positiveNumber = 1;
            int negativeNumber = -1;

            boolean enough;
            boolean change = true;

            do {
                // eltolás léptetése
                if (change) {
                    shift = positiveNumber;
                    positiveNumber++;
                } else {
                    shift = negativeNumber;
                    negativeNumber--;
                }
                change = !change;

                //Szöveg bevitele


                String fileName1 = "C:/Users/Tibor/Downloads/codemessage.txt";
                File file = new File(fileName1);
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while((line = br.readLine()) != null){
                    messageToCipher = line;
                }

                // Kódfejtés
                textContent = cipher(messageToCipher, shift);

                //szöveg kiírtaása véleményezés előtt "kódfejtés után"
                if (textContent.length() >= 24) {
                    // első két tucat karakter kiíratása
                    System.out.println("A dekódolt szöveg: " + textContent.substring(0, 24) + "...\n");
                } else {
                    // ha a szöveg karaktereinek a száma nem egyenlő vagy haladja meg a 24-t (ne fusson out of bounds hibára)
                    System.out.println(textContent + "\n");
                }

                writeTextToFile(fileName, textContent);

                // kérdés a továbblépésről
                answer = askNextRound(scanner, shift, textContent);
                System.out.println();
                enough = (Objects.equals(answer, "i"));
            } while (enough);
        }
    }

//    private static String readMessage() {

//        return """
//                6EYÁ ÖÍŰ6Y:ÁŐ-ŐÁÜÁEUYÍQ.Á-Y6Y P8ÖY QŰÁ-,:+EPÍYÓ69Á:ÁŐÁYÚÖ-Á:ŐÁ9-Á YÓ6+E PÜ-
//                6Y6YÖÖ0YAÖÜPÍÓP7,:V76 0Y6Y+ETAÁ-+QÍÁ+YÚS9ÉÁŐ-UÚY6EYÁEEÁÜYÚS9,Ü-YWEÁ Á-ÁÚ\s
//                ÁÚY6YŰÁÍÉÁŐ-Q+Q:ÁYÁÜU+ET:YÁÜÁÚ-:,ŰÁ8Ó6 ÖÚ%+YÚS9-T:UYÍQ.ÁÚÁ-ZYÚQ+U77YÁÜÁÚ-:, ÖÚ%
//                +Y+EPŰR-SÍQ.Á-YÚ, +-:%PÜ-6Ú0YÁEY%-S77ÖYA,Ü-YÁÍDQ7ÚQ -Y6YA6Ü6Ó6YÚQ+ER-Á--
//                YÁÜ+UYÁÜÁÚ-:, ÖÚ%+Y+EPŰR-SÍQ.0Y6YÓP7,:V Y7ÁÜWÜYÚWÜT Y4YŰ6YVÍDYŰ, 96 P ÚY4YÖ É,:Ű6-
//                ÖÚ6ÖY7ÖE-, +PÍÖYÓ69AÖ+ÁÜQ+YÉ,ÜD-Y6Y+ETAÁ-+QÍÁ+ÁÚYQ+Y6Y QŰÁ-ÁÚYÚTE-0YÁ  ÁÚYŰÁÍ\s
//                DÁ:Q+QAÁÜY6Y+ETAÁ-+QÍÁ+YÚS9ÉÁŐ-UÚY4Y-T:-Q Q+EÖYÚ, +EÁ E%+Y+EÁ:Ö -Y4YÜÁÍ6ÜP77YÚQ-
//                YQAAÁÜYÜÁ:TAÖ9R-Á--QÚY6YAÖÜPÍÓP7,:V-0Y6Y+ETAÁ-+QÍÁ+ÁÚYÉTÜQ DÁYÁY-Q:Á YÜÁÍ.:ÁÍ P\s
//                +6776 Y6EY6-Ü6 -Ö4S8ÁP ÖYÓ69ŰXAÁÜÁ-ÁÚY+,:P Y DÖÜAP %Ü-YŰÁÍ2Y6EYQAÁÚÖÍYÖÍÁ YÓ6-QÚ,\s
//                DY QŰÁ-Y-Á ÍÁ:6Ü6--ŐP:SYÉÜ,--P-YÁÍDYÖ9UY%-P Y-ÁÜŐÁ+Á YÉÁÜŰ,:E+,Ü-PÚY:ÁŐ-ŐÁÜÁEÁ--
//                YWEÁ Á-ÁÖÚYÖ.6:+EÁ:XYAÖ++E6ÉÁŐ-Q+Q:ÁY6Ü6.,EA60Y,ÜD6  DÖ:6ZYÓ,ÍDY6Y QŰÁ-Y69ŰÖ:6ÜÖ-
//                P+Y4YŰP:YÁÍDYQAAÁÜY6Y ,:Ű6 9Ö6ÖY.6:-:6+EPÜÜP+YÁÜU--Y4YÜÁYÖ+YPÜÜR-,--6Y6YŰÁÍŰ6:69-
//                Y7VAP: 6+EP9,ÚY7ÁAÁ-Q+Q-0Y6::SÜY6E, 76 ZYÓ,ÍDYŰÖQ:-YŰ6:69-6ÚY6Ü%ÜZY+ÁŐ-
//                ÁÜŰWÚY+ÁŰYA,Ü-0Y6YAÖÜPÍYÚTEAQÜÁŰQ DÁYÓ,++EVYQA-ÖEÁ9ÁÚÚÁÜY6YÓP7,:VYÜÁEP:P+P-YÚTAÁ-UÁ
//                YQ:-Á+WÜÓÁ-Á--Y8+6ÚY6YÜ, 9, Ó,EYÚTEÁÜÖY7ÜÁ-8ÓÜÁDY.6:Ú76 YŰXÚT9UYÚS9ÉÁŐ-UY8+,.,:-
//                YÜQ-ÁEQ+Q:UÜYQ+Y-ÁAQÚÁ D+QÍQ:UÜ0""";
//    }

    private static String interlace(String kit1, String kit2) {

        // kit1 String to char array
        char[] characters1 = new char[kit1.length()];


        for (int i = 0; i < kit1.length(); i++) {
            char character = kit1.charAt(i);
            characters1[i] = character;
        }

        // kit2 String to char array
        char[] characters2 = new char[kit2.length()];

        for (int i = 0; i < kit2.length(); i++) {
            char character = kit2.charAt(i);
            characters2[i] = character;
        }

        // cipzárszerű összefűzés a két karaktertömbön
        int sumOfKitsLength = kit1.length() + kit2.length();
        char[] finalCharArray = new char[sumOfKitsLength];

        for (int i = 0; i < kit1.length(); i++) {
            finalCharArray[i + i] = characters2[i];
            finalCharArray[(i + i) + 1] = characters1[i];
        }

        // "%" hozzáadása a tömbhöz
        finalCharArray[52] = characters2[26];

        // char array to String
        StringBuilder zipStrings = new StringBuilder();
        for (char c : finalCharArray) {
            zipStrings.append(c);
        }

        // visszatérés Stringgel
        return zipStrings.toString();
    }

    private static String askNextRound(Scanner scanner, int shift, String textContent) {
        boolean rule;
        String answer;

        do {
            System.out.println("Szeretné újra lefuttatni módosított eltolással? (i/n)");
            System.out.print("Válasz: ");
            answer = scanner.nextLine();
            System.out.println();
            rule = answer.equals("i") || answer.equals("n");
            if (!rule) {
                System.out.println("Csak az alábbi lehetőseg elfogadhatóak: i(igen), n(nem)");
            }
        } while (!rule);
        if (!Objects.equals(answer, "i")) {
            programEnd(shift, textContent);
        }
        return answer;
    }

    private static String cipher(String messageToCipher, int shift) {
        String kit1 = basicLatinLetters;
       String kit2 = numerals + ACCENTED_CHARS + PUNCTUATIONS;

        //teljes jelkészlet String formájában
        String zipString = interlace(kit1, kit2);

        // loop a szöveg titkosításához/visszafejtéséhez
        messageToCipher = messageToCipher.toUpperCase();
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageToCipher.length(); i++) {
            int currentPosition = zipString.indexOf(messageToCipher.charAt(i));
            int partialResult = (currentPosition - shift) % 53;
            if (partialResult < 0) {
                partialResult = zipString.length() + partialResult;
            }
            char replaceVal = zipString.charAt(partialResult);
            message.append(replaceVal);
        }
        return message.toString();
    }

    private static void writeTextToFile(String fileName, String textContent) {
        try {
            Files.writeString(Path.of(fileName), textContent, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            System.err.println("Hiba történt fájl írásakor: " + ex.getMessage());
        }
    }

    private static void programEnd(int shift, String textContetnt) {
        System.out.println("\nAz eltolás mértéke: " + shift + "\nA teljes szöveg: " + textContetnt + "\nViszlát!");
        exit(1);
    }
}