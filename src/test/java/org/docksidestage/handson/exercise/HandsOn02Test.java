package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.unit.UnitContainerTestCase;

/**
 * @author black-trooper
 */
public class HandsOn02Test extends UnitContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                      テストデータの閲覧
    //                                                                      ==============
    public void test_existsTestData() {
        // ## Act ##
        int count = memberBhv.selectCount(cb -> {});

        // ## Assert ##
        assertTrue(count > 0);
    }

    public void test_selectMemberNameStartWithSS() {
        // ## Arrange ##
        String prefix = "S";

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().setMemberName_LikeSearch(prefix, op -> op.likePrefix());
            cb.query().addOrderBy_MemberName_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            assertTrue(member.getMemberName().startsWith(prefix));
        });
    }

    public void test_selectMemberIdFirst() {
        // ## Act ##
        memberBhv.selectEntity(cb -> {
            cb.query().setMemberId_Equal(1);
        }).alwaysPresent(member -> {
            // ## Assert ##
            Integer memberId = member.getMemberId();
            assertNotNull(memberId);
            assertTrue(memberId.equals(1));
        });
    }

    public void test_select_生年月日がない会員を検索() {
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().setBirthdate_IsNull();
            cb.query().addOrderBy_UpdateDatetime_Desc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        for (Member member : memberList) {
            assertNull(member.getBirthdate());
            log(member.getUpdateDatetime());
        }
    }
}
