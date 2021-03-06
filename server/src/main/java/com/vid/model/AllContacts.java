package com.vid.model;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.*;

/**
 * Created by song on 17-2-12.
 * <p>
 * 所有联系人
 */
@SuppressWarnings("unchecked")
public class AllContacts {

    /**
     * userID，对应id字段
     */
    private int userID;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户所有分组组名列表
     */
    private List<Group> groupList;

    /**
     * 所有联系人列表，按联系人姓名首字母分为27个list
     * 第0~25对应A～Z
     * 第26项为数字、特殊字符
     */
    private List[] contacts = new List[27];

    /**
     * 初始化list
     */
    private void init(List<Contact> contactList) {
        for (int i = 0; i < contacts.length; i++) {
            contacts[i] = new ArrayList();
        }

        try {
            for (Contact contact : contactList) {
                // 获取第一个字的拼音
                String pinyin = PinyinHelper.getShortPinyin(contact.getNoteName().substring(0, 1));
                char first = Character.toLowerCase(pinyin.charAt(0));

                if (Character.isLetter(first)) {
                    contacts[first - 'a'].add(contact);
                } else {
                    contacts[contacts.length - 1].add(contact);
                }
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对同一list内的联系人进行排序
     */
    private void sortContacts() {
        Comparator<Contact> comparator = (contact1, contact2) -> {
            String pinyin1, pinyin2;

            try {
                pinyin1 = PinyinHelper.convertToPinyinString(contact1.getNoteName(), "", PinyinFormat.WITHOUT_TONE);
                pinyin2 = PinyinHelper.convertToPinyinString(contact2.getNoteName(), "", PinyinFormat.WITHOUT_TONE);

                return pinyin1.compareTo(pinyin2);
            } catch (PinyinException e) {
                e.printStackTrace();
            }

            return 0;
        };

        for (List contact : contacts) {
            contact.sort(comparator);
        }
    }

    public AllContacts(int userID, String name, List<Contact> contactList, List<Group> groupList) {
        this.userID = userID;
        this.name = name;
        this.groupList = groupList;

        // 初始化
        init(contactList);
        // 排序
        sortContacts();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List[] getContacts() {
        return contacts;
    }

    public void setContacts(List[] contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "AllContacts{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", groupList=" + groupList +
                ", contacts=" + Arrays.toString(contacts) +
                '}';
    }
}
