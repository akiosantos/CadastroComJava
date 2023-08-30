package com.br.var.solutions.adapters.input.contollers;

import com.br.var.solutions.*;

import com.br.var.solutions.application.services.use.MundialUseCase;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.Objects;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin(origins = "*")
@Slf4j
public class PessoaController {

    //            1               2                3                 4
    //publico/privado //tipo de retorno //nome do método // parâmetros

    @Autowired
    private MundialUseCase mundialUseCase;

    //Endpoint

    @GetMapping
    public ResponseEntity<Object> get() {

        PessoaRequest pessoaRequest1 = new PessoaRequest();
        pessoaRequest1.setNome("Lucas");
        pessoaRequest1.setSobrenome("Akio");
        pessoaRequest1.setEndereco("Av. Zélia, 500");
        pessoaRequest1.setIdade(23);

        return ResponseEntity.ok(pessoaRequest1);
    }

    @GetMapping("/resumo")
    public ResponseEntity<Object> getPessoa(@RequestBody PessoaRequest pessoinha, @RequestParam(value = "valida_mundial") boolean DesejaValidarMundial) {
        InformacoesIMC imc = new InformacoesIMC();
        int anoNascimento = 0;
        InformacoesIR impostoRenda = new InformacoesIR();
        String validarMundial = null;
        String saldoEmDolar = null;


        if (!pessoinha.getNome().isEmpty()) {

            log.info("Iniciando o processo de resumo da pessoa", pessoinha);

            if (Objects.nonNull(pessoinha.getPeso()) && Objects.nonNull(pessoinha.getAltura())) {
                log.info("Iniciando o calculo do IMC");
                imc = calculaImc(pessoinha.getPeso(), pessoinha.getAltura());
            }

            if (Objects.nonNull(pessoinha.getIdade())) {
                log.info("Iniciando o calculo do ano de nascimento");
                anoNascimento = calculaAnoNascimento(pessoinha.getIdade());
            }

            if (Objects.nonNull(pessoinha.getSalario())) {
                log.info("Iniciando o calculo de imposto de renda");
                impostoRenda = calculaFaixaImpostoRenda(pessoinha.getSalario());
            }

            if (Boolean.TRUE.equals(DesejaValidarMundial)) {
                if (Objects.nonNull(pessoinha.getTime())) {
                    log.info("Validando se o time de coração tem Mundial");
                    validarMundial = mundialUseCase.calculoMundial(pessoinha.getTime());
                }
            }

            if (Objects.nonNull(pessoinha.getSaldo())) {
                log.info("Convertendo real em Dólar");
                saldoEmDolar = converteSaldoEmDolar(pessoinha.getSaldo());
            }

            log.info("Montando Objeto de retorno para o front-end");
            PessoaResponse resumo = montarRespostaFronEnd(pessoinha, imc, anoNascimento, impostoRenda, validarMundial, saldoEmDolar);

            return ResponseEntity.ok(resumo);
        }

        return ResponseEntity.noContent().build();
    }

    private String converteSaldoEmDolar(double saldo) {
        return String.valueOf(saldo / 5.11);
    }

    private PessoaResponse montarRespostaFronEnd(PessoaRequest pessoa, InformacoesIMC imc, int anoNascimento, InformacoesIR impostoRenda, String validarMundial, String saldoEmDolar) {

        PessoaResponse response = new PessoaResponse();

        response.setNome(pessoa.getNome());

        response.setImc(imc.getImc());
        response.setClassificacaoIMC(imc.getClassificacao());
        //response.setSalario(impostoRenda); erro não arrumado

        response.setIR(impostoRenda.getCalculoIR());
        response.setAliquota(impostoRenda.getAliquota());

        response.setAnoNascimento(anoNascimento);
        response.setMundialClubes(validarMundial);
        response.setSaldoEmDolar(saldoEmDolar);
        response.setIdade(pessoa.getIdade());
        response.setTime(pessoa.getTime());
        response.setSobrenome(pessoa.getSobrenome());
        response.setEndereco(pessoa.getEndereco());
        response.setAltura(pessoa.getAltura());
        response.setPeso(pessoa.getPeso());
        response.setSaldo(pessoa.getSaldo());

        return response;
    }


    //Regra: Base de calculo é Salario Bruto x Aliquota - dedução.
    private InformacoesIR calculaFaixaImpostoRenda(double salario) {
        log.info("Iniciando o calculo do Imposto de renda: " + salario);
        String novoSalarioCalculado;

        InformacoesIR ir = new InformacoesIR();

        if (salario <= 1903.98) {
            ir.setCalculoIR(Double.parseDouble(String.valueOf(ir.getIsento())));
            ir.setAliquota("isento");
            return ir;


        } else if (salario > 1903.98 && salario < 2826.65) {
            ir.calculoIR = (salario / 100) * ir.getLiquota1();
            ir.setLiquota1(Double.parseDouble(String.valueOf(ir.getCalculoIR())));
            ir.setAliquota("7,5%");

            return ir;


        } else if (salario >= 2826.66 && salario < 3751.05) {
            ir.calculoIR = (salario / 100) * ir.getLiquota2();
            ir.setLiquota2(Double.parseDouble(String.valueOf(ir.getCalculoIR())));
            ir.setAliquota("15%");

            return ir;


        } else if (salario >= 3751.06 && salario < 4664.68) {
            ir.calculoIR = (salario / 100) * ir.getLiquota3();
            ir.setLiquota3(Double.parseDouble(String.valueOf(ir.getCalculoIR())));
            ir.setAliquota("22,5%");

            return ir;

        } else {
            double calculoIRF = 869.36 - ((salario * 0.275) / 100);
            ir.calculoIR = (salario / 100) * ir.getLiquota4();
            ir.setLiquota4(Double.parseDouble(String.valueOf(ir.getCalculoIR())));
            ir.setAliquota("27,5%");

            return ir;

        }
    }

    private int calculaAnoNascimento(int idade) {
        LocalDate datalocal = LocalDate.now();
        int anoAtual = datalocal.getYear();
        return anoAtual - idade;
    }

    private InformacoesIMC calculaImc(double peso, double altura) {
        double imc = peso / (altura * altura);

        InformacoesIMC imcCalculado = new InformacoesIMC();

        if (imc <= 18.5) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("abaixo do peso.");
            return imcCalculado;

        } else if (imc >= 18.5 && imc <= 24.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("peso ideal.");
            return imcCalculado;

        } else if (imc > 24.9 && imc <= 29.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("excesso de peso.");
            return imcCalculado;

        } else if (imc > 29.9 && imc <= 34.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("obesidade classe I.");
            return imcCalculado;

        } else if (imc > 34.9 && imc < 39.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("obesidade classe II.");
            return imcCalculado;

        } else {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("obesidade classe III.");
            return imcCalculado;
        }

    }

//    @DeleteMapping
//    public void retornoDelete() {
//
//    }
//
//    @PutMapping
//    public void retornoPut() {
//
//    }
//
//    @PostMapping
//    public void retornoPost() {
//
//    }

//    @PostMapping(path = "/authorization", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//    public ResponseEntity<GenerateToken> authorization(@RequestParam("client_id") String clientId,
//                                                @RequestParam("password") String password) {
//
//        log.info("Iniciando a tentativa de geração de token para o usuário: " + clientId);
//
//        Boolean validaUsuario = ValidaUsuario.validaUsuario(clientId, password);
//
//        if(Boolean.FALSE.equals(validaUsuario)){
//            log.error("Não foi possível gerar o token, pois o usuário ou a senha são incorretos.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenerateToken());
//        }
//
//        long expirationDate = System.currentTimeMillis() + 1800000;
//
//        String token = Jwts.builder()
//                .setSubject(clientId)
//                .setExpiration(new Date(expirationDate))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//
//        GenerateToken tokenResponse = new GenerateToken();
//
//        tokenResponse.setToken(token);
//        tokenResponse.setExpiraEm(new Date(expirationDate));
//        tokenResponse.setTempoValidacao(expirationDate);
//        tokenResponse.setSolicitante(clientId);
//
//        log.info("token gerado com sucesso para o usuário: " + clientId + " Em: " + System.currentTimeMillis());
//
//        return ResponseEntity.ok(tokenResponse);
//    }

//    @PostMapping(path = "/authenticate", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//    public ResponseEntity<GenerateToken> gerenerateToken(@RequestParam("client_id") String clientId,
//                                                         @RequestParam("password") String password) {
//
//        log.info("Iniciando a tentativa de geração de token para o usuário: " + clientId);
//
//        Boolean validaUsuario = ValidaUsuario.validaUsuario(clientId, password);
//
//        if(Boolean.FALSE.equals(validaUsuario)){
//            log.error("Não foi possível gerar o token, pois o usuário ou a senha são incorretos.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenerateToken());
//        }
//
//        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//
//        String token = jwtTokenUtil.generateToken(clientId);
//
//        GenerateToken tokenResponse = new GenerateToken();
//        tokenResponse.setToken(token);
//
//        log.info("token gerado com sucesso para o usuário: " + clientId + " Em: " + System.currentTimeMillis());
//
//        return ResponseEntity.ok(tokenResponse);
//    }
}