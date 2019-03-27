package com.bowfletchers.chatberry.Testing;

import com.bowfletchers.chatberry.ClassLibrary.Chat;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;

import java.util.ArrayList;

public class MockData {

    public static Member buildUserObject() {
        Member testMember = new Member("TestName");
        Member friend1 = new Member("friend1");
        Member friend2 = new Member("friend2");
        Member friend3 = new Member("friend3");
        testMember.setProfilePicture(0);
        testMember.addFriend(friend1);
        testMember.addFriend(friend2);
        testMember.addFriend(friend3);

   /*     // chat with friend 1
        ArrayList<Member> chat1Members = new ArrayList<>();
        chat1Members.add(testMember);
        chat1Members.add(friend1);
        Chat chat1 = new Chat("ChatWithFriend1", chat1Members);
        testMember.addChat(chat1);

        // chat with friend 2
        ArrayList<Member> chat2Members = new ArrayList<>();
        chat2Members.add(testMember);
        chat2Members.add(friend2);
        Chat chat2 = new Chat("ChatWithFriend2", chat2Members);
        testMember.addChat(chat2);

        // chat with friend 3
        ArrayList<Member> chat3Members = new ArrayList<>();
        chat3Members.add(testMember);
        chat3Members.add(friend3);
        Chat chat3 = new Chat("ChatWithFriend3", chat3Members);
        testMember.addChat(chat3);

        // message for chat 1 from testUser to Friend1
        Message message_chat1_1 = new Message(testMember, "hello 1");
        Message message_chat1_2 = new Message(friend1, "hello back 1");
        Message message_chat1_3 = new Message(testMember, "bye 1");
        chat1.addMessage(message_chat1_1);
        chat1.addMessage(message_chat1_2);
        chat1.addMessage(message_chat1_3);

        // message for chat 2 from testUser to Friend2
        Message message_chat2_1 = new Message(testMember, "hello 2");
        Message message_chat2_2 = new Message(friend1, "hello back 2");
        Message message_chat2_3 = new Message(testMember, "bye 2");
        chat1.addMessage(message_chat2_1);
        chat1.addMessage(message_chat2_2);
        chat1.addMessage(message_chat2_3);

        // message for chat 3 from testUser to Friend3
        Message message_chat3_1 = new Message(testMember, "hello 3");
        Message message_chat3_2 = new Message(friend1, "hello back 3");
        Message message_chat3_3 = new Message(testMember, "bye 3");
        chat1.addMessage(message_chat3_1);
        chat1.addMessage(message_chat3_2);
        chat1.addMessage(message_chat3_3);*/

        return testMember;
    }
}
