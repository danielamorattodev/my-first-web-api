package dio.web.api.repository;

import dio.web.api.handler.CampoObrigatorioException;
import dio.web.api.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioRepository {
    public void save(Usuario usuario){ //--Tratamento de exceções: Campo obrigatório --.
        if(usuario.getLogin()==null)
            throw new CampoObrigatorioException("login");

        if(usuario.getPassword()==null)
            throw new CampoObrigatorioException("password");

         System.out.println("SAVE - Recebendo o usuário na camada de repositório"); //método POST.
         System.out.println(usuario);
    }

    public void update(Usuario usuario){
         System.out.println("UPDATE - Recebendo o usuário na camada de repositório"); //método PUT
         System.out.println(usuario);
    }
    public void deleteById(Integer id){ //método DELETE
        System.out.println(String.format("DELETE/id - Recebendo o id: %d para excluir um usuário", id));
        System.out.println(id);
    }
    public List<Usuario> findAll(){ //método GET All
        System.out.println("LIST - Listando os usários do sistema");
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("daniela","password"));
        usuarios.add(new Usuario("erick","masterpass"));
        return usuarios;
    }
    public Usuario findById(Integer id){ //método GetOne.
        System.out.println(String.format("FIND/id - Recebendo o id: %d para localizar um usuário", id));
        return new Usuario("daniela","password");
    }
    public Usuario findByUsername(String username){
        System.out.println(String.format("FIND/username - Recebendo o usernamae: %s para localizar um usuário", username));
        return new Usuario("daniela","password");
    }


}
