package vn.devpro.devprokidorigin.controllers.hoctap;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.entity.hocchucai.Question;

/**
 * Created by hoang-ubuntu on 21/03/2018.
 */

public class QuestionLoadingTask extends AsyncTask<Void, Void, List<Object>> {

    private IQuestionLoadingListener listener;
    private DBHelper dbHelper;
    private List<Object> list;

    public QuestionLoadingTask(IQuestionLoadingListener listener, DBHelper dbHelper) {
        this.listener = listener;
        this.dbHelper = dbHelper;
        list = new ArrayList<>();
    }

    @Override
    protected List<Object> doInBackground(Void... voids) {
        /*Thuc thi xay dung cau hoi*/
        QuestionProvider.getInstance(dbHelper).updateListQuestion();
        list.add(QuestionProvider.getInstance(dbHelper).getNumberQuestion());
        list.add(QuestionProvider.getInstance(dbHelper).builQuestionFromList());
        list.add(QuestionProvider.getInstance(dbHelper).isShowQuestion());
        return list;
    }

    @Override
    protected void onPostExecute(List<Object> questions) {
        if (listener != null) {
            listener.onResultQuestion((List<Question>) questions.get(1));
            listener.getNumberQuestion((Integer) questions.get(0));
            listener.isShowQuestion((Integer) questions.get(2));
        }
    }

    public interface IQuestionLoadingListener {
        void onResultQuestion(List<Question> questions);
        void getNumberQuestion(int number);
        void isShowQuestion(int isShow);
    }
}
