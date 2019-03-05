package lebonsandwich.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lpro.lebonsandwich.boundary.CategorieResource;
import org.lpro.lebonsandwich.entity.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/**
 *
 * @author dufour76u
 */
public class CategorieRepresentationTests {
    @Autowired
    private CategorieResource cr;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setupContext(){
        cr.deleteAll();
    }

    @Test
    public void getOneCategorie(){
        Categorie c = new Categorie("burgers","les burgers");
        c.setId(UUID.randomUUID().toString());
        cr.save(c);
        ResponseEntity<String> response = restTemplate.getForEntity("/categories/"+c.getId(),String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("les burgers");

    }

    @Test
    public void getAllCategorie(){
        Categorie c = new Categorie("burgers","les burgers");
        c.setId(UUID.randomUUID().toString());
        cr.save(c);
        Categorie c2 = new Categorie("traditionnels","les sandwichs classiques");
        c2.setId(UUID.randomUUID().toString());
        cr.save(c2);

        ResponseEntity<String> response = restTemplate.getForEntity("/categories",String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("burgers");
        assertThat(response.getBody()).contains("classiques");
        List<String> list = JsonPath.read(response.getBody(),"$.._embedded..categories");     
        assertThat(list).asList().hasSize(2);
    }

    @Test
    public void postCategorie() throws Exception{
        Categorie c = new Categorie("burgers", "les burgers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(this.toJsonString(c),headers);

        ResponseEntity<?> response = restTemplate.postForEntity("/categories", entity, ResponseEntity.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = response.getHeaders().getLocation();
        response = restTemplate.getForEntity(location,String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String toJsonString(Object o) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(o);
    }
}
