import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;

public class BaseModel extends BaseDaoEnabled<BaseModel, String> {
    @DatabaseField(generatedId = true)
    public int pk;
}
