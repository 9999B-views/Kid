package vn.devpro.devprokidorigin.controllers.hoctap;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.entity.hocchucai.Question;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by hoang-ubuntu on 20/03/2018.
 */

public class QuestionProvider {
    private String TAG = QuestionProvider.class.getSimpleName();
    private DBHelper dbHelper;
    private List<Question> listQuestionPrio0, listQuestionPrio1, listQuestionPrio2;
    private List<Question> listAllQuestion;

    private static QuestionProvider instance;

    public static QuestionProvider getInstance(DBHelper dbHelper) {
        if (instance == null) {
            instance = new QuestionProvider(dbHelper);
        }
        return instance;
    }

    private QuestionProvider() {
    }

    private QuestionProvider(DBHelper dbHelper) {
        this.dbHelper = dbHelper;

        listAllQuestion = new ArrayList<>();
        listQuestionPrio0 = new ArrayList<>();
        listQuestionPrio1 = new ArrayList<>();
        listQuestionPrio2 = new ArrayList<>();
        listAllQuestion = getAllQuestion(1);

        updateListQuestion();
    }

    private List<Question> getAllQuestion(int topic_id) {
        List<Question> list = new ArrayList<>();
        list = dbHelper.getAllQuestion(topic_id);
        return list;
    }

    private List<Question> getListQuestion(int topic_id, int show_ratio, int correct_count) {
        List<Question> list = new ArrayList<>();
        list = dbHelper.getQuestion(topic_id, show_ratio, correct_count);
        return list;
    }

    public void updateListQuestion() {
        listQuestionPrio0 = getListQuestion(1, 1, 0);
        listQuestionPrio1 = getListQuestion(1, 1, 1);
        listQuestionPrio2 = getListQuestion(1, 1, 2);
    }

    private boolean checkListQuestionProi(List<Question> list) {
        /*Kiem tra danh sach null?*/
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    private int getNumberRandom(int a) {
        if (a <= 0) {
            return 0;
        }
        int numb;
        Random random = new Random();
        numb = random.nextInt(a);
        return numb;
    }

    private int getChooseNumber() {
        int numb = getNumberRandom(10);

        if (0 <= numb && numb <= 4) {
            return 1;
        } else if (5 <= numb && numb <= 7) {
            return 2;
        } else {
            return 3;
        }
    }

    public List<Question> builQuestionFromList() {
        List<Question> questionList = new ArrayList<>();
        int choose = getChooseNumber();
        switch (choose) {
            case 1:
                questionList = getQuestion(listQuestionPrio0, listQuestionPrio1, listQuestionPrio2);
                break;
            case 2:
                questionList = getQuestion(listQuestionPrio1, listQuestionPrio0, listQuestionPrio2);
                break;
            case 3:
                questionList = getQuestion(listQuestionPrio2, listQuestionPrio0, listQuestionPrio1);
                break;
            default:
                break;
        }

        return questionList;
    }

    private List<Question> getQuestion(List<Question> list0, List<Question> list1, List<Question> list2) {
        List<Question> questionList = new ArrayList<>();
        if (checkListQuestionProi(list0) == true) {
            int questionID = getNumberRandom(list0.size() - 1);
            questionList.add(list0.get(questionID));
        } else {
            questionList = builQuestionAgain1(list1, list2);
        }
        if (questionList.size() > 0) {
            buildThreeWrongQuestion(questionList.get(0).getId(), questionList);
        }

        return questionList;
    }

    private List<Question> builQuestionAgain1(List<Question> list1, List<Question> list2) {
        List<Question> resultList = new ArrayList<>();
        int numb = getNumberRandom(9);
        int id = -1;
        if (0 <= numb && numb <= 6) {
            if (checkListQuestionProi(list1)) {
                id = getNumberRandom(list1.size() - 1);
                resultList.add(list1.get(id));
            } else if (checkListQuestionProi(list2)) {
                id = getNumberRandom(list2.size() - 1);
                resultList.add(list2.get(id));
            } else {
            }
        } else {
            if (checkListQuestionProi(list2)) {
                id = getNumberRandom(list2.size() - 1);
                resultList.add(list2.get(id));
            } else if (checkListQuestionProi(list1)) {
                id = getNumberRandom(list1.size() - 1);
                resultList.add(list1.get(id));
            } else {
            }
        }
        return resultList;
    }

    private void removeElementList(List<Question> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                list.remove(i);
            }
        }
    }

    private void buildThreeWrongQuestion(int questionID, List<Question> resultList) {
        int index = -1;
        List<Question> listQuestionTemp = new ArrayList<>();
        for (int i = 0; i < listQuestionPrio0.size(); i++) {
            listQuestionTemp.add(listQuestionPrio0.get(i));
        }
        for (int i = 0; i < listQuestionPrio1.size(); i++) {
            listQuestionTemp.add(listQuestionPrio1.get(i));
        }
        for (int i = 0; i < listQuestionPrio2.size(); i++) {
            listQuestionTemp.add(listQuestionPrio2.get(i));
        }
        removeElementList(listQuestionTemp, questionID);

        if (getNumberQuestion() < 4) {
            return;
        } else {
        /*Lay ra 3 dap an sai*/
            index = getNumberRandom(listQuestionTemp.size() - 1);
            resultList.add(listQuestionTemp.get(index));
            removeElementList(listQuestionTemp, listQuestionTemp.get(index).getId());

            index = getNumberRandom(listQuestionTemp.size() - 1);
            resultList.add(listQuestionTemp.get(index));
            removeElementList(listQuestionTemp, listQuestionTemp.get(index).getId());

            index = getNumberRandom(listQuestionTemp.size() - 1);
            resultList.add(listQuestionTemp.get(index));
            removeElementList(listQuestionTemp, listQuestionTemp.get(index).getId());
        }
    }

    public int getNumberQuestion() {
        int count = 0;
        count = count + listQuestionPrio0.size() + listQuestionPrio1.size() + listQuestionPrio2.size();
        return count;
    }

    public int isShowQuestion() {
        switch (getRanDomNumber()) {
            case 1:
                return 0;
            case 2:
                return xacSuatVaoCauHoi(4);
            case 3:
                return xacSuatVaoCauHoi(6);
            case 4:
                return xacSuatVaoCauHoi(8);
            default:
                return 0;
        }
    }

    private int randomNumber(int a) {
        if (a <= 0)
            return 0;
        Random random = new Random();
        int rd = random.nextInt(a);
        return rd;
    }

    private int xacSuatVaoCauHoi(int a) {
        int random = randomNumber(9);
        if (random < a) {
            return 1;
        }
        return 0;
    }

    private int getRanDomNumber() {
        int numberQuestion = getNumberQuestion();
        if (numberQuestion < 4) {
            return 1;
        } else if (4 <= numberQuestion && numberQuestion <= 14) {
            return 2;
        } else if (15 <= numberQuestion && numberQuestion <= 23) {
            return 3;
        } else {
            return 4;
        }
    }
}
