package dio.web.api.handler;

//Tratamento de exceções global, para interceptar todas as exceções que ocorrem em nossa aplicação, sem precisar tornar declarativo em todos os recursos.

// -- Esta classe captará, todas as exceções de negócio "BusinessException",para tratar e converter e retornar mensagens mais declarativas ao usuário da aplicação.

import javax.annotation.Resource;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;
    private HttpHeaders headers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ResponseError responseError(String message,HttpStatus statusCode){
        ResponseError responseError = new ResponseError();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        return responseError;
    }

    @ExceptionHandler(Exception.class) //verifica se essa execeção é business exception.
    private ResponseEntity<Object> handleGeneral(Exception e, WebRequest request) {
        if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
            return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
        } else {
            String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
            ResponseError error = responseError(message,HttpStatus.INTERNAL_SERVER_ERROR);//Tratamento de erro interno, no caso de NÃO ser uma business exception.
            return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler({BusinessException.class})
    private ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        ResponseError error = responseError(e.getMessage(),HttpStatus.CONFLICT); //No caso de Business Exception todas as mensagens de erro vão retornar para o cliente uma mensagem de conflito.
        return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
    }

}

//Esclarececendo alguns pontos:
//Linha 24: O método headers() retorna um objeto do tipo Headers;
//Linha 31: O método responseError() retorna o corpo do erro da aplicação;
//Linha 40: O método handleGeneral() intercepta as exceções do sistema e verifica se é uma exceção genérica ou de negócio;
//Linha 52: O método handleBusinessException() é destinado a criar um ResponseEntity contendo o nosso ResponseError devidamente estruturado.