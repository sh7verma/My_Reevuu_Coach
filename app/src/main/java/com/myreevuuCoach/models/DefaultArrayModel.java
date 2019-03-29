package com.myreevuuCoach.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dev on 4/2/19.
 */

public class DefaultArrayModel extends BaseModel implements Parcelable {

    /**
     * response : {"levels":[{"id":12,"name":"High School","sport_id":1,"user_type":1,"text_field":0,"level_type":1,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":13,"name":"JR College","sport_id":1,"user_type":1,"text_field":0,"level_type":1,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":14,"name":"College","sport_id":1,"user_type":1,"text_field":0,"level_type":1,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":15,"name":"Minor League","sport_id":1,"user_type":1,"text_field":0,"level_type":1,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":16,"name":"Major League / Professional","sport_id":1,"user_type":1,"text_field":0,"level_type":1,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"}],"coaching_levels":[{"id":25,"name":"Youth","sport_id":1,"user_type":0,"text_field":0,"level_type":2,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":26,"name":"High School","sport_id":1,"user_type":0,"text_field":0,"level_type":2,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":27,"name":"College","sport_id":1,"user_type":0,"text_field":1,"level_type":2,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":28,"name":"Minor Leagues","sport_id":1,"user_type":0,"text_field":0,"level_type":2,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"},{"id":29,"name":"Major Leagues","sport_id":1,"user_type":0,"text_field":0,"level_type":2,"created_at":"2019-02-01T10:05:13.000Z","updated_at":"2019-02-01T10:05:13.000Z"}],"certificates":[{"id":1,"name":"USA Baseball Courses","options":"","certificate_type":2,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":2,"name":"SafeSport","options":"","certificate_type":0,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":3,"name":"PCA Double-Goal","options":"PCA - Level 1$#split#$PCA - Level 2$#split#$PCA - Level 3","certificate_type":1,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":4,"name":"Babe Ruth League / Cal Ripken","options":"","certificate_type":0,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":5,"name":"ABCA Coaching Credits","options":"","certificate_type":0,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":6,"name":"Baseball Ace","options":"","certificate_type":0,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":7,"name":"American Coaching Academy","options":"","certificate_type":0,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"},{"id":8,"name":"NFHS Coaching Baseball","options":"","certificate_type":0,"sport_id":1,"user_type":1,"created_at":"2019-02-01T10:05:11.000Z","updated_at":"2019-02-01T10:05:11.000Z"}],"experties":[{"id":1,"name":"Batting mechanics (right hand)","sport_id":1,"color":1},{"id":2,"name":"Batting mechanics (left hand)","sport_id":1,"color":2},{"id":3,"name":"Hitting Off-speed","sport_id":1,"color":3},{"id":4,"name":"Pitching (right hand)","sport_id":1,"color":4},{"id":5,"name":"Pitching (left hand)","sport_id":1,"color":5},{"id":6,"name":"Curveball","sport_id":1,"color":6},{"id":7,"name":"Fastball","sport_id":1,"color":0},{"id":8,"name":"Changeup","sport_id":1,"color":1},{"id":9,"name":"Groundballs","sport_id":1,"color":2},{"id":10,"name":"Flyballs","sport_id":1,"color":3},{"id":11,"name":"Pick-off moves","sport_id":1,"color":4},{"id":12,"name":"Training techniques","sport_id":1,"color":5}],"questions":[{"id":1,"question":"Do you prefer a 02-04 12:26:01.704 27264-27523/com.myreevuuCoach D/OkHttp:  male or female instructor?","question_type":1,"option":"MALE,FEMALE,DOSN'T MATTER","answers":""},{"id":2,"question":"What area(s) are you most interested in improving?","question_type":2,"option":"Batting mechanics (right hand),Batting mechanics (left hand),Hitting Off-speed,Pitching (right hand),Pitching (left hand),Curveball,Fastball,Changeup,Groundballs,Flyballs,Pick-off moves,Training techniques","answers":""},{"id":4,"question":"Do you currently have a coach / instructor?","question_type":1,"option":"YES,NO","answers":""},{"id":5,"question":"What is your USTA rating?","question_type":1,"option":"2.5-3.0,3.5,4.0,4.5,5.0,NOT RATED","answers":""},{"id":6,"question":"Do you play?","question_type":1,"option":"SINGLES,DOUBLES,BOTH","answers":""}],"competes":[{"id":1,"name":"Local","sport_id":0,"user_type":0,"created_at":"2019-02-01T10:05:15.000Z","updated_at":"2019-02-01T10:05:15.000Z"},{"id":2,"name":"State","sport_id":0,"user_type":0,"created_at":"2019-02-01T10:05:15.000Z","updated_at":"2019-02-01T10:05:15.000Z"},{"id":3,"name":"Regional","sport_id":0,"user_type":0,"created_at":"2019-02-01T10:05:15.000Z","updated_at":"2019-02-01T10:05:15.000Z"},{"id":4,"name":"National","sport_id":0,"user_type":0,"created_at":"2019-02-01T10:05:15.000Z","updated_at":"2019-02-01T10:05:15.000Z"},{"id":5,"name":"International","sport_id":0,"user_type":0,"created_at":"2019-02-01T10:05:15.000Z","updated_at":"2019-02-01T10:05:15.000Z"}],"states":[]}
     * code : 111
     */

    private ResponseBean response;
    private int code;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResponseBean {
        private List<LevelsBean> levels;
        private List<CoachingLevelsBean> coaching_levels;
        private List<CertificatesBean> certificates;
        private List<ExpertiesBean> experties;
        private List<QuestionsBean> questions;
        private List<CompetesBean> competes;
        private List<OptionsModel> states;

        public List<LevelsBean> getLevels() {
            return levels;
        }

        public void setLevels(List<LevelsBean> levels) {
            this.levels = levels;
        }

        public List<CoachingLevelsBean> getCoaching_levels() {
            return coaching_levels;
        }

        public void setCoaching_levels(List<CoachingLevelsBean> coaching_levels) {
            this.coaching_levels = coaching_levels;
        }

        public List<CertificatesBean> getCertificates() {
            return certificates;
        }

        public void setCertificates(List<CertificatesBean> certificates) {
            this.certificates = certificates;
        }

        public List<ExpertiesBean> getExperties() {
            return experties;
        }

        public void setExperties(List<ExpertiesBean> experties) {
            this.experties = experties;
        }

        public List<QuestionsBean> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionsBean> questions) {
            this.questions = questions;
        }

        public List<CompetesBean> getCompetes() {
            return competes;
        }

        public void setCompetes(List<CompetesBean> competes) {
            this.competes = competes;
        }

        public List<OptionsModel> getStates() {
            return states;
        }

        public void setStates(List<OptionsModel> states) {
            this.states = states;
        }

        public static class LevelsBean implements Parcelable {
            /**
             * id : 12
             * name : High School
             * sport_id : 1
             * user_type : 1
             * text_field : 0
             * level_type : 1
             * created_at : 2019-02-01T10:05:13.000Z
             * updated_at : 2019-02-01T10:05:13.000Z
             */

            private int id;
            private String name;
            private int sport_id;
            private int user_type;
            private int text_field;
            private int level_type;
            private String created_at;
            private String updated_at;


            private int color;
            private boolean isSelected;

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSport_id() {
                return sport_id;
            }

            public void setSport_id(int sport_id) {
                this.sport_id = sport_id;
            }

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            public int getText_field() {
                return text_field;
            }

            public void setText_field(int text_field) {
                this.text_field = text_field;
            }

            public int getLevel_type() {
                return level_type;
            }

            public void setLevel_type(int level_type) {
                this.level_type = level_type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.sport_id);
                dest.writeInt(this.user_type);
                dest.writeInt(this.text_field);
                dest.writeInt(this.level_type);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
                dest.writeInt(this.color);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            }

            public LevelsBean() {
            }

            protected LevelsBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.sport_id = in.readInt();
                this.user_type = in.readInt();
                this.text_field = in.readInt();
                this.level_type = in.readInt();
                this.created_at = in.readString();
                this.updated_at = in.readString();
                this.color = in.readInt();
                this.isSelected = in.readByte() != 0;
            }

            public static final Creator<LevelsBean> CREATOR = new Creator<LevelsBean>() {
                @Override
                public LevelsBean createFromParcel(Parcel source) {
                    return new LevelsBean(source);
                }

                @Override
                public LevelsBean[] newArray(int size) {
                    return new LevelsBean[size];
                }
            };
        }

        public static class CoachingLevelsBean implements Parcelable {
            /**
             * id : 25
             * name : Youth
             * sport_id : 1
             * user_type : 0
             * text_field : 0
             * level_type : 2
             * created_at : 2019-02-01T10:05:13.000Z
             * updated_at : 2019-02-01T10:05:13.000Z
             */

            private int id;
            private String name;
            private int sport_id;
            private int user_type;
            private int text_field;
            private int level_type;
            private String created_at;
            private String updated_at;
            private int color;
            private boolean isSelected;

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSport_id() {
                return sport_id;
            }

            public void setSport_id(int sport_id) {
                this.sport_id = sport_id;
            }

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            public int getText_field() {
                return text_field;
            }

            public void setText_field(int text_field) {
                this.text_field = text_field;
            }

            public int getLevel_type() {
                return level_type;
            }

            public void setLevel_type(int level_type) {
                this.level_type = level_type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.sport_id);
                dest.writeInt(this.user_type);
                dest.writeInt(this.text_field);
                dest.writeInt(this.level_type);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
                dest.writeInt(this.color);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            }

            public CoachingLevelsBean() {
            }

            protected CoachingLevelsBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.sport_id = in.readInt();
                this.user_type = in.readInt();
                this.text_field = in.readInt();
                this.level_type = in.readInt();
                this.created_at = in.readString();
                this.updated_at = in.readString();
                this.color = in.readInt();
                this.isSelected = in.readByte() != 0;
            }

            public static final Creator<CoachingLevelsBean> CREATOR = new Creator<CoachingLevelsBean>() {
                @Override
                public CoachingLevelsBean createFromParcel(Parcel source) {
                    return new CoachingLevelsBean(source);
                }

                @Override
                public CoachingLevelsBean[] newArray(int size) {
                    return new CoachingLevelsBean[size];
                }
            };
        }

        public static class CertificatesBean implements Parcelable {
            /**
             * id : 1
             * name : USA Baseball Courses
             * options :
             * certificate_type : 2
             * sport_id : 1
             * user_type : 1
             * created_at : 2019-02-01T10:05:11.000Z
             * updated_at : 2019-02-01T10:05:11.000Z
             */

            private int id;
            private String name;
            private String options;
            private int certificate_type;
            private int sport_id;
            private int user_type;
            private String created_at;
            private String updated_at;

            private int color;
            private boolean isSelected;

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOptions() {
                return options;
            }

            public void setOptions(String options) {
                this.options = options;
            }

            public int getCertificate_type() {
                return certificate_type;
            }

            public void setCertificate_type(int certificate_type) {
                this.certificate_type = certificate_type;
            }

            public int getSport_id() {
                return sport_id;
            }

            public void setSport_id(int sport_id) {
                this.sport_id = sport_id;
            }

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeString(this.options);
                dest.writeInt(this.certificate_type);
                dest.writeInt(this.sport_id);
                dest.writeInt(this.user_type);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
                dest.writeInt(this.color);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            }

            public CertificatesBean() {
            }

            protected CertificatesBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.options = in.readString();
                this.certificate_type = in.readInt();
                this.sport_id = in.readInt();
                this.user_type = in.readInt();
                this.created_at = in.readString();
                this.updated_at = in.readString();
                this.color = in.readInt();
                this.isSelected = in.readByte() != 0;
            }

            public static final Creator<CertificatesBean> CREATOR = new Creator<CertificatesBean>() {
                @Override
                public CertificatesBean createFromParcel(Parcel source) {
                    return new CertificatesBean(source);
                }

                @Override
                public CertificatesBean[] newArray(int size) {
                    return new CertificatesBean[size];
                }
            };
        }

        public static class ExpertiesBean implements Parcelable {
            /**
             * id : 1
             * name : Batting mechanics (right hand)
             * sport_id : 1
             * color : 1
             */

            private int id;
            private String name;
            private int sport_id;
            private int color;
            private boolean isSelected;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSport_id() {
                return sport_id;
            }

            public void setSport_id(int sport_id) {
                this.sport_id = sport_id;
            }

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.sport_id);
                dest.writeInt(this.color);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            }

            public ExpertiesBean() {
            }

            protected ExpertiesBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.sport_id = in.readInt();
                this.color = in.readInt();
                this.isSelected = in.readByte() != 0;
            }

            public static final Creator<ExpertiesBean> CREATOR = new Creator<ExpertiesBean>() {
                @Override
                public ExpertiesBean createFromParcel(Parcel source) {
                    return new ExpertiesBean(source);
                }

                @Override
                public ExpertiesBean[] newArray(int size) {
                    return new ExpertiesBean[size];
                }
            };
        }

        public static class QuestionsBean implements Parcelable {
            /**
             * id : 1
             * question : Do you prefer a 02-04 12:26:01.704 27264-27523/com.myreevuuCoach D/OkHttp:  male or female instructor?
             * question_type : 1
             * option : MALE,FEMALE,DOSN'T MATTER
             * answers :
             */

            private int id;
            private String question;
            private int question_type;
            private String option;
            private String answers;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public int getQuestion_type() {
                return question_type;
            }

            public void setQuestion_type(int question_type) {
                this.question_type = question_type;
            }

            public String getOption() {
                return option;
            }

            public void setOption(String option) {
                this.option = option;
            }

            public String getAnswers() {
                return answers;
            }

            public void setAnswers(String answers) {
                this.answers = answers;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.question);
                dest.writeInt(this.question_type);
                dest.writeString(this.option);
                dest.writeString(this.answers);
            }

            public QuestionsBean() {
            }

            protected QuestionsBean(Parcel in) {
                this.id = in.readInt();
                this.question = in.readString();
                this.question_type = in.readInt();
                this.option = in.readString();
                this.answers = in.readString();
            }

            public static final Creator<QuestionsBean> CREATOR = new Creator<QuestionsBean>() {
                @Override
                public QuestionsBean createFromParcel(Parcel source) {
                    return new QuestionsBean(source);
                }

                @Override
                public QuestionsBean[] newArray(int size) {
                    return new QuestionsBean[size];
                }
            };
        }

        public static class CompetesBean implements Parcelable {
            /**
             * id : 1
             * name : Local
             * sport_id : 0
             * user_type : 0
             * created_at : 2019-02-01T10:05:15.000Z
             * updated_at : 2019-02-01T10:05:15.000Z
             */

            private int id;
            private String name;
            private int sport_id;
            private int user_type;
            private String created_at;
            private String updated_at;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSport_id() {
                return sport_id;
            }

            public void setSport_id(int sport_id) {
                this.sport_id = sport_id;
            }

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.sport_id);
                dest.writeInt(this.user_type);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
            }

            public CompetesBean() {
            }

            protected CompetesBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.sport_id = in.readInt();
                this.user_type = in.readInt();
                this.created_at = in.readString();
                this.updated_at = in.readString();
            }

            public static final Creator<CompetesBean> CREATOR = new Creator<CompetesBean>() {
                @Override
                public CompetesBean createFromParcel(Parcel source) {
                    return new CompetesBean(source);
                }

                @Override
                public CompetesBean[] newArray(int size) {
                    return new CompetesBean[size];
                }
            };
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) this.response, flags);
        dest.writeInt(this.code);
    }

    public DefaultArrayModel() {
    }

    protected DefaultArrayModel(Parcel in) {
        this.response = in.readParcelable(ResponseBean.class.getClassLoader());
        this.code = in.readInt();
    }

    public static final Parcelable.Creator<DefaultArrayModel> CREATOR = new Parcelable.Creator<DefaultArrayModel>() {
        @Override
        public DefaultArrayModel createFromParcel(Parcel source) {
            return new DefaultArrayModel(source);
        }

        @Override
        public DefaultArrayModel[] newArray(int size) {
            return new DefaultArrayModel[size];
        }
    };
}
