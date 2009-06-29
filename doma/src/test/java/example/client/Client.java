package example.client;

import org.seasar.doma.domain.IntegerDomain;

import example.dao.EmpDao;
import example.dao.EmpDao_;

/**
 * @author taedium
 * 
 */
public class Client {

    public static void main(String[] args) {
        EmpDao dao = new EmpDao_();
        dao.selectById(new IntegerDomain(1), null);
    }
}
