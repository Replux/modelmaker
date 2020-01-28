# API
roundEnv.getRootElements() //获取项目下的所有类


# Experiment
> 2020-1-27 16:39:50
1. 单项目 TestProcessor >>> OK
2. 单项目 TestProcessor/ALLARGS >>> OK >>> 1.6
3. 双项目 TestProcessor/ALLARGS >>> OK >>> 1.6
4. 双项目 TestProcessor/ALLARGS/BUILDER >> error >>> 1.7
5. 双项目 TestProcessor/ALLARGS >>> OK >>> 1.6
6. 单项目 TestProcessor/ALLARGS/BUILDER >> OK >> 1.8
PS: 使用Builder的前提是AllArgs存在

7. 单项目 ALLProcessor >> OK >> 1.8

8. lombok 1.9 >>> OK

# JCTree
modelMaker:StudentDO()
mods:@ModelMaker(birthPlace = "cn.replux.model", characteristics = {"Data", "Builder"}, mapper = {@FieldMapping(from = "name", toName = "name", toType = String.class), @FieldMapping(from = "age", toName = "age", toType = Integer.class), @FieldMapping(from = "male", toName = "male", toType = Boolean.class), @FieldMapping(from = "grade", toName = "grade", toType = Integer.class)})
, 
restype:void, typarams:[], recvparam:null,params:[],thrown:[],body:{
}


modelMaker:StudentDTO()
mods:@ModelMaker(mapper = {@FieldMapping(from = "name", toName = "mingzi", toType = String.class), @FieldMapping(from = "age", toName = "nianling", toType = Integer.class), @FieldMapping(from = "male", toName = "xingbie", toType = Boolean.class)})
, restype:void, typarams:[], recvparam:null,params:[],thrown:[],body:{
}


modelMaker:StudentVO()
mods:@ModelMaker(mapper = {@FieldMapping(from = "name", toType = String.class), @FieldMapping(from = "age", toType = Integer.class), @FieldMapping(from = "male", toType = Boolean.class)})
, restype:void, typarams:[], recvparam:null,params:[],thrown:[],body:{
}

# experiment2
modelMaker:StudentDO()
mods:@ModelMaker(birthPlace = "cn.replux.model", characteristics = {"Data", "Builder"}, mapper = {@FieldMapping(from = "name", toName = "name", toType = String.class), @FieldMapping(from = "age", toName = "age", toType = Integer.class), @FieldMapping(from = "male", toName = "male", toType = Boolean.class), @FieldMapping(from = "grade", toName = "grade", toType = Integer.class)})
, restype:void, typarams:[], recvparam:null,params:[],thrown:[],body:{
}

modelMaker:StudentVO(java.lang.String,int)
mods:@ModelMaker(birthPlace = "cn.replux.model")
, restype:void, typarams:[], recvparam:null,params:[String name, int age],thrown:[],body:{
    String mingzi = translate(name, String.class);
    Long nianling = translate(age, Long.class);
}

# experiment 3
PS: 构造函数的restype==null

# experiment 4
14:49:18.444 [main] DEBUG cn.replux.modelmaker.processor.BaseProcessor - enter ModelTemplate
14:49:18.506 [main] DEBUG cn.replux.modelmaker.processor.BaseProcessor - 
 annotations1:[@ModelMaker(birthPlace = "cn.replux.model", characteristics = {"Data", "Builder"})]
14:49:18.507 [main] DEBUG cn.replux.modelmaker.processor.BaseProcessor - 
 annotations1:[@ModelMaker()]

# experiment 5
 methodDecl:
@ModelMaker(birthPlace = "cn.replux.model", characteristics = {"Data", "Builder"})
void StudentDO() {
    translate(name, "name", String.class);
    translate(age, "age", long.class);
    add("height", Integer.class);
    reduce(grade);
}
 lhs:birthPlace,rhs:"cn.replux.model"
 lhs:characteristics,rhs:{"Data", "Builder"}
 ___ 
 methodDecl:
@ModelMaker()
void StudentVO() {
    translate(name, "mingzi", String.class);
    translate(age, "nianling", long.class);
    add("height", Integer.class);
}


