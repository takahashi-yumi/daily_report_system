package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.LikeConverter;
import actions.views.LikeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.JpaConst;
import models.Like;

    /**
     * いいねテーブルの操作に関わる処理を行うクラス
     */
    public class LikeService extends ServiceBase {

        /**
         * 指定した日報(employee→report)をいいね（report→like）した従業員の件数を一覧画面に表示する分取得し
         * LikeViewのリストで返却する
         //@param report 日報
         //@param page ページ数
         //@return 一覧画面に表示するデータのリスト
         */
        public List<LikeView> getMinePerPage(ReportView report, int page) {

            List<Like> likecounts = em.createNamedQuery(JpaConst.Q_LIK_GET_ALL_MINE, Like.class)
                    .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                    .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                    .setMaxResults(JpaConst.ROW_PER_PAGE)
                    .getResultList();
            return LikeConverter.toViewList(likecounts);
        }

        /**
         * 指定した日報をいいねした従業員の件数を取得し、返却する
         //* @param report
         * @return いいねした従業員の件数
         * (employee→report)（report→like）
         */
        public long countAllMine(ReportView report) {

            long count = (long) em.createNamedQuery(JpaConst.Q_LIK_COUNT_ALL_MINE, Long.class)
                    .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                    .getSingleResult();

            return count;
        }

        /**
         * 指定されたページ数の一覧画面に表示するいいねデータを取得し、LikeViewのリストで返却する
         * @param page ページ数
         * @return 一覧画面に表示するデータのリスト
         */
        public List<LikeView> getAllPerPage(int page) {

            List<Like> likecounts = em.createNamedQuery(JpaConst.Q_LIK_GET_ALL, Like.class)
                    .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                    .setMaxResults(JpaConst.ROW_PER_PAGE)
                    .getResultList();
            return LikeConverter.toViewList(likecounts);
        }

        /**
         * いいねテーブルのデータの件数を取得し、返却する
         * @return データの件数
         */
        public long countAll() {
            long likecounts_count = (long) em.createNamedQuery(JpaConst.Q_LIK_COUNT, Long.class)
                    .getSingleResult();
            return likecounts_count;
        }

        /**
         * idを条件に取得したデータをLikeViewのインスタンスで返却する
         * @param id
         * @return 取得データのインスタンス
         */
        public LikeView findOne(int id) {
            return LikeConverter.toView(findOneInternal(id));

          //セッションから日報の情報を取得
            ReportView rv = (ReportView) session.getAttribute(AttributeConst.REP_ID);

          //日報をいいねした従業員データを、指定されたページ数の一覧画面に表示する分取得する
            int page = getPage();
            List<LikeView> likes = service.getMinePerPage(ReportView, page);

          //指定した日報のいいねの件数を取得
            long myLikesCount = likeService.countAllMine(ReportView report);

            putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
            putRequestScope(AttributeConst.LIK_COUNT, myLikesCount); //日報のいいねの数
            putRequestScope(AttributeConst.PAGE, page); //ページ数
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数


            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }

            //いいねした人一覧画面を表示
            forward(ForwardConst.FW_LIK_SHOW);
        }

        /**
         * 画面から入力されたいいねの登録内容を元にデータを1件作成し、
         * いいねテーブルに登録する
         * @param rv いいねの登録内容
         */
        public void create(LikeView lv) {

                LocalDateTime ldt = LocalDateTime.now();
                lv.setCreatedAt(ldt);
                lv.setUpdatedAt(ldt);
                createInternal(lv);
        }

        /**
         * 画面から入力されたいいねの登録内容を元に、いいねデータを更新する
         * @param rv いいねの更新内容
         */
        public void update(LikeView lv) {

                //更新日時を現在時刻に設定
                LocalDateTime ldt = LocalDateTime.now();
                lv.setUpdatedAt(ldt);

                updateInternal(lv);
        }

        /**
         * idを条件にデータを1件取得する
         * @param id
         * @return 取得データのインスタンス
         */
        private Like findOneInternal(int id) {
            return em.find(Like.class, id);
        }

        /**
         * いいねデータを1件登録する
         * @param rv いいねデータ
         */
        private void createInternal(LikeView lv) {

            em.getTransaction().begin();
            em.persist(LikeConverter.toModel(lv));
            em.getTransaction().commit();

        }

        /**
         * 日報データを更新する
         * @param rv いいねデータ
         */
        private void updateInternal(LikeView lv) {

            em.getTransaction().begin();
            Like l = findOneInternal(lv.getId());
            LikeConverter.copyViewToModel(l, lv);
            em.getTransaction().commit();

        }

    }

