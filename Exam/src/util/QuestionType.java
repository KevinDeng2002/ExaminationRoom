package util;

public enum QuestionType {
    SELECTION,JUDGEMENT,FILLING;
    public static QuestionType intToType(int n){
        switch (n){
            case 1:
                return SELECTION;
            case 2:
                return JUDGEMENT;
            case 3:
                return FILLING;
            default:
                throw new RuntimeException("Illegal question type.");
        }
    }
}
