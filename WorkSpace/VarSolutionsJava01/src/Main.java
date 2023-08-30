
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Nome da classe ( Departamento )
public class Main {

    //Nome do método - Recurso dentro da classe (O que o departamento faz)
    public static void main(String[] args) {
        /*
        System.out.println("1: Olá Mundo!!!");
        System.out.println("2: Este é o meu primeiro código em Java");

//          1                 2                 3
    //Tipo da variável | Nome da Váriavel | Valor da variável

        String olaMundo = "3: Olá Mundo, esta mensagem está dentro da variável ";
        System.out.println(olaMundo);

        String nome = "Lucas";
        String sobrenome = "Akio";
        String idade = "22";


        System.out.println(nome);
        System.out.println(sobrenome);

        //Juntar as variáveis:
        System.out.println(nome + " "+ sobrenome + " " + "tem" + " " + idade + " " + "anos" + ".");
        */

//        String retornoMetodo = buscarSobrenome();
//        System.out.println(retornoMetodo);

//        String retornoMetodo2 = inserirNome();
//        System.out.println(retornoMetodo2);

        double imc = calcularIMC();

        int idade = retornarIdadePorAno();

        List<String> retornoMensagem = resumoPessoa();
        System.out.println(retornoMensagem);

    }

//         1           2                    3              4
//    tipo acesso   tipo de retorno   nome do método   parâmetros

      public static String buscarSobrenome(){
        String sobrenome = "Akio Cadastro";
        return sobrenome;
    }

    public static String inserirNome(){

        Scanner ler = new Scanner(System.in);

        System.out.printf("Digite seu nome: ");
        String nome = ler.next();

        System.out.printf("Digite seu sobrenome: ");
        String sobrenome = ler.next();

//        System.out.println(nome + " " + sobrenome);

        String nomeCompleto = nome + " " + sobrenome;

        return nomeCompleto;
    }

    public static double calcularIMC(){

        Scanner ler = new Scanner(System.in);

        System.out.printf("Digite sua altura:");
        double altura = ler.nextDouble();

        System.out.printf("Digite o seu peso:");
        double peso = ler.nextDouble();

        double calculoIMC = peso / (altura * altura);

        System.out.println("O seu IMC é: " + calculoIMC);

        if(calculoIMC < 19.0) {
            System.out.println("Você está abaixo do peso!");
        }else{
            System.out.println("Você está acima do peso");
        }

        return calculoIMC;
    }

    public static int retornarIdadePorAno() {
        int idadeCalculada;

        Scanner ler = new Scanner(System.in);

        System.out.printf("Digite seu nome: ");
        String nome = ler.next();

        System.out.printf("Digite seu ano de nascimento, Ex: 1990: ");
        int anoBase = ler.nextInt();

        idadeCalculada = 2023 - anoBase;

        String tipoDePessoas;

        String maiorDeDezoito = "você é maior de 18 anos";
        String menorDeDezoito = "você é menor de 18 anos, caí fora!!!";

        if (idadeCalculada <=17){
            tipoDePessoas = menorDeDezoito;
        }else{
            tipoDePessoas = maiorDeDezoito;
        }

        System.out.println(nome + " sua idade é: "+ idadeCalculada + " anos e " + tipoDePessoas);

        return idadeCalculada;
    }

    public static List<String> resumoPessoa(){

        Scanner ler = new Scanner(System.in);

        List<String> nomesResumo = new ArrayList<>();

        //        1   ;   2  ;  3  ;
        for (int indice = 1; indice <= 4; indice ++){

            System.out.printf("Digite seu nome: ");
            String nome = ler.next();

            System.out.printf("Digite seu sobrenome: ");
            String sobrenome = ler.next();

            System.out.printf("Digite sua idade: ");
            int idade = ler.nextInt();

            String nomeCompleto = nome + " " + sobrenome;

            String tipoDePessoa = "";

            if (idade <= 17){
                tipoDePessoa = "menor de idade.";
            }else {
                tipoDePessoa = "maior de idade.";
            }

            String resumo = "O nome completo é: " + nomeCompleto + " ele tem " +  idade + ", e ele é: " + tipoDePessoa;

            nomesResumo.add(resumo);
        }
        return nomesResumo;
    }

}
