/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Максим Усков
 */
import java.util.Scanner;
public class Parser {
    private String Output;                                                       //Выходная строка после проверки на правильность записи и преобразования в обратную польскую запись
    private Stack<Character> stackSymbol;                                        //Стак содержащий символы(Необходим для создания обратной польской записи)
    private Stack<Double> stackNumber;
    private boolean FlagOrderOfRecording; 
    /*Проверка на правильную последовательность записи
    * 0 -> Предыдущий элемент является символом
    * 1 -> Предыдущий элемент является числом/переменной
    * В качесте начального значения возьмем 0. Для того что бы избежать случаев где выражение начинается с символа
    *( '-' и '(' будут рассматриваться отдельно)
    */
    private int PrioritySymbol;
    /*
    * Приоритет символов:
    * ^   -> 4
    * *,/ -> 3
    * +,- -> 2
    * (,) -> 1
    */
    Parser(){
       stackSymbol=new Stack<Character>();
       stackNumber=new Stack<Double>();
       Output="";
    }
    public double calculations(String Expression){                               //Функция которая вызывает основные функции
        Expression.replaceAll("\\s","");
        ReversePolishNotation(Expression);
        return CalculatingValue();
    }

    private void ReversePolishNotation(String Expression){                       //Приводим уравнение к Обратному польскому виду
        boolean boolFirstSymbol=true;                                            //Служит для определения первый это символ или нет(необходимо для нахождения унарного минуса)
        while(Expression.length()!=0){                                           //While до конца строки Expression
            char ch = Expression.charAt(0);                                      //Выбирает первый символ
            switch(ch){                                                          //Разбор и преобразование выражения по Алгоритму Преобразование из инфиксной нотации в ОПЗ
                case '+': {                                                       //Дальнейшие кейсы будут работать по одинаковому принцепу кроме не которых случаев
                    if(!FlagOrderOfRecording){                                   //Проверяет что бы перед символом было число/переменная
                        throw new IllegalStateException("Выражение записанно некорректно1");
                    }
                    if(PrioritySymbol>2){                                        //Проверка ранга операции
                        Output+=stackSymbol.pop();
                    }
                    FlagOrderOfRecording=false;             
                    stackSymbol.push(ch);
                    PrioritySymbol=2;
                    boolFirstSymbol=false;
                    break;
                }
                case '-': {
                    if(boolFirstSymbol){                                         //Запоминания унарного минуса
                        Output+='"';
                        break;
                    }
                    if(!FlagOrderOfRecording){
                        throw new IllegalStateException("Выражение записанно некорректно2");
                    }
                    if(PrioritySymbol>2){
                        Output+=stackSymbol.pop();
                    }
                    FlagOrderOfRecording=false;
                    stackSymbol.push(ch);
                    PrioritySymbol=2;  
                    boolFirstSymbol=false;
                    break;
                }
                case '*': {
                    if(!FlagOrderOfRecording){
                        throw new IllegalStateException("Выражение записанно некорректно3");
                    }
                    if(PrioritySymbol>3){
                        Output+=stackSymbol.pop();
                    }
                    FlagOrderOfRecording=false;
                    stackSymbol.push(ch);
                    PrioritySymbol=3;  
                    boolFirstSymbol=false;
                    break;
                }
                case '/': {
                    if(!FlagOrderOfRecording){
                        throw new IllegalStateException("Выражение записанно некорректно4");
                    }
                    if(PrioritySymbol>=3){
                        Output+=stackSymbol.pop();
                    }
                    FlagOrderOfRecording=false;
                    stackSymbol.push(ch);
                    PrioritySymbol=3;  
                    boolFirstSymbol=false;
                    break;
                }
                case '(':{ 
                    if(FlagOrderOfRecording){
                        throw new IllegalStateException("Выражение записанно некорректно5");
                    }
                    FlagOrderOfRecording=false;
                    stackSymbol.push(ch);
                    PrioritySymbol=1;
                    boolFirstSymbol=true;
                    break;
                }
                case ')': {
                    if(!FlagOrderOfRecording){
                        throw new IllegalStateException("Выражение записанно некорректно6");
                    }
                    while(!stackSymbol.comparison('(') && !stackSymbol.empty()){ //Поиск открывающей скобки
                        Output+=stackSymbol.pop();
                    }
                    if(stackSymbol.empty()){
                        throw new IllegalStateException("Выражение записанно некорректно7");
                    }
                    stackSymbol.pop();                                           //Удаление Открывающую скобку
                    PrioritySymbol=1;
                    boolFirstSymbol=false;
                    break;
                }
                case '^': {
                    if(!FlagOrderOfRecording){
                        throw new IllegalStateException("Выражение записанно некорректно8");
                    }
                    if(PrioritySymbol>4){
                        Output+=stackSymbol.pop();
                    }
                    FlagOrderOfRecording=false;
                    stackSymbol.push(ch);
                    PrioritySymbol=4;  
                    boolFirstSymbol=false;
                    break;
                }
                default:{
                    if(ch=='"' || FlagOrderOfRecording )                         //Проверяет что бы перед чеслом/переменной был символ 
                        throw new IllegalStateException("Выражение записанно некорректно9");
                    Output+=ch;
                    boolFirstSymbol=false;
                    FlagOrderOfRecording=true;
                }
            }
            Expression=removeCharAt(Expression,0);                               //Удаление первого символа в строке
        }
        while(!stackSymbol.empty()){                                             //Перенос символов из стека в строку
            if(stackSymbol.comparison('('))                                      //Проверка на наличие лишней открывающей скобки
                throw new IllegalStateException("Выражение записанно некорректно10");
            Output+=stackSymbol.pop();
        }
        
    }
    
    private double CalculatingValue(){                                           //Вычисление значения выражения из ОПЗ
        double tmp;                                                              //Вспомогательная переменная
        while(Output.length()!=0){                                               //Цикл до опусташения строки Output
            char ch = Output.charAt(0);                                          //Выбирает первый символ
            switch(ch){                                                          //Разбор Output по символьно
                case '"':{                                                       //Определение унарного минуса
                    Output=removeCharAt(Output,0);                               //Удаление пометки(")
                    ch=Output.charAt(0);                                
                    tmp=Variable(ch);                                            //Определение числа(если число то символ переводиться в число, если переменная то она задается пользователем)    
                    stackNumber.push(-1*tmp);                                    //Заполнение стека значений
                    break;
                }
                case'+':{
                    tmp=stackNumber.pop()+stackNumber.pop();
                    stackNumber.push(tmp);
                    break;
                }
                case'-':{
                    tmp=stackNumber.pop();
                    tmp=stackNumber.pop()-tmp;
                    stackNumber.push(tmp);
                    break;
                }
                case'*':{
                    tmp=stackNumber.pop()*stackNumber.pop();
                    stackNumber.push(tmp);
                    break;
                }
                case'/':{
                    tmp=stackNumber.pop();
                    if(stackNumber.peek()==0)                                    //Определение делителя(0)
                        throw new IllegalStateException("Выражение записанно некорректно11");
                    tmp=stackNumber.pop()/tmp;
                    stackNumber.push(tmp);
                    break;
                }
                case'^':{
                    tmp=stackNumber.pop();
                    tmp=Math.pow(stackNumber.pop(),tmp);
                    stackNumber.push(tmp);
                    break;
                }
                default:{
                    tmp=Variable(ch);
                    stackNumber.push(tmp);
                        break;
                }
            }
            Output=removeCharAt(Output,0);
        }
        return stackNumber.pop();
    }
    private static double Variable(char ch){                                     //Определение Переменной/числа                    
        double tmp;
        if(ch>=48 && ch<=57){
            tmp=Character.getNumericValue(ch);
            return tmp;
        }
        System.out.print("Введите значение переменной "+ ch+"\n");
        Scanner scan = new Scanner(System.in);
        tmp=scan.nextDouble();
        return tmp;
    }
    private static String removeCharAt(String s, int pos) {                      //Удаление первого символа в строке
      return s.substring(0, pos) + s.substring(pos + 1);
    }
}