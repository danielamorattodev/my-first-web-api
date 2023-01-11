package dio.web.api.handler;

//Classe para tratar de exceções de login e senha. Exemplo de classe mãe caso tenha mais de um campo obrigatório para ser preenchido pelo cliente na aplicação.

public class CampoObrigatorioException extends BusinessException {
    public CampoObrigatorioException(String campo) {
        super("O Campo %s é obrigatório" , campo);
    }
}
