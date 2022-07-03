package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Like;

/**
 * いいねデータのDTOモデル⇔Viewモデルの変換を行うクラス
 */
public class LikeConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param lv LikeViewのインスタンス
     * @return Likeのインスタンス
     */
    public static Like toModel(LikeView lv) {

        return new Like(
                lv.getId(),
                lv.getEmployee(),
                lv.getReport(),
                lv.getCreatedAt(),
                lv.getUpdatedAt());
    }
    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param l Likeのインスタンス
     * @return LikeViewのインスタンス
     */
    public static LikeView toView(Like l) {
        if (l == null) {
            return null;
        }

        return new LikeView(
                l.getId(),
                l.getEmployee(),
                l.getReport(),
                l.getCreatedAt(),
                l.getUpdatedAt());
    }
    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<LikeView> toViewList(List<Like> list) {
        List<LikeView> lvs = new ArrayList<>();

        for (Like l : list) {
            lvs.add(toView(l));
        }
        return lvs;
    }
    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param l DTOモデル(コピー先)
     * @param lv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Like l, LikeView lv) {
        l.setId(lv.getId());
        l.setEmployee(lv.getEmployee());
        l.setReport(lv.getReport());
        l.setCreatedAt(lv.getCreatedAt());
        l.setUpdatedAt(lv.getUpdatedAt());

        //r.setEmployee(EmployeeConverter.toModel(rv.getEmployee()));
    }


}
