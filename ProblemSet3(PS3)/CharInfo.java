public class CharInfo {
    private Character letter;
    private Integer frequency;

    public CharInfo(Character letter, Integer frequency){
        this.letter= letter;
        this.frequency = frequency;
    }

    public Character getName(){
        return letter;
    }

    public Integer getFrequency(){
        return frequency;
    }

    public void setName(Character name){
        this.letter = name;
    }

    public void setFrequency(Integer frequency){
        this.frequency = frequency;
    }


    public String toString(Character letter, Integer frequency){
        return letter + " " + frequency;
    }

}
