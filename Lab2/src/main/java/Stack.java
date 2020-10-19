/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Максим Усков
 */
public class Stack<T> {              //Стек который в качестве элемента хранит элементы любого типа
    private class Node{                 //Класс который хранит элементы Стека
        private T element;
        private Node next;
        Node(T e){
        element=e;
        next=null;
        }
        Node(){
        }
    }
    private Node stack;
    Stack(){
        stack=new Node();
    }
    public void push(T str){       //Добавление элемента в Стек
        Node tmp=new Node(str);
        tmp.next=stack;
        stack=tmp;
    }
     public T pop(){               //Получение ,хранящегося в последнем элементе, значения и удаления данного элемента Стека
        T str=stack.element;
        stack=stack.next;
        return(T) str;
    }
      public T peek(){             //Получение значения хранящегося последнего элемента Стека
        return(T) stack.element;
    }
      public boolean empty(){
          if(stack==null || stack.element==null)
              return true;
          return false;
      }
      public boolean comparison(T str){
          if(str==stack.element)
              return true;
          return false;
      }
}
