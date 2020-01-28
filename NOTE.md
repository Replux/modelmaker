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
14:49:18.444  - enter ModelTemplate
14:49:18.506  - 
 annotations1:[@ModelMaker(birthPlace = "cn.replux.model", characteristics = {"Data", "Builder"})]
14:49:18.507  - 
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


# experiment

birthPlace:"cn.replux.model",characteristics:{"Data", "Builder"}
birthPlace:null,characteristics:null

# experiment
20:21:11.242  - birthPlace:null,characteristics:null
20:21:11.243  - methodDecl.body:{
    translate(name, "mingzi", String.class);
    translate(age, "nianling", long.class);
    add("height", Integer.class);
}
stat:translate(name, "mingzi", String.class);
stat:translate(age, "nianling", long.class);
stat:add("height", Integer.class);

birthPlace:"cn.replux.model",characteristics:{"Data", "Builder"}
20:21:11.245  - methodDecl.body:{
    translate(name, "name", String.class);
    translate(age, "age", long.class);
    add("height", Integer.class);
    reduce(grade);
}
stat:translate(name, "name", String.class);
stat:translate(age, "age", long.class);
stat:add("height", Integer.class);
stat:reduce(grade);


# experiment 
birthPlace:"cn.replux.model",characteristics:{"Data", "Builder"}
20:31:06.519  - methodDecl.body:{
    translate(name, "name", String.class);
    translate(age, "age", long.class);
    add("height", Integer.class);
    reduce(grade);
}
20:31:06.520  - stat:translate(name, "name", String.class);
20:31:06.521  - expressionStatement.expr:translate(name, "name", String.class)
20:31:06.521  - stat:translate(age, "age", long.class);
20:31:06.521  - expressionStatement.expr:translate(age, "age", long.class)
20:31:06.521  - stat:add("height", Integer.class);
20:31:06.521  - expressionStatement.expr:add("height", Integer.class)
20:31:06.522  - stat:reduce(grade);
20:31:06.522  - expressionStatement.expr:reduce(grade)
20:31:06.522  - birthPlace:null,characteristics:null
20:31:06.522  - methodDecl.body:{
    translate(name, "mingzi", String.class);
    translate(age, "nianling", long.class);
    add("height", Integer.class);
}
20:31:06.522  - stat:translate(name, "mingzi", String.class);
20:31:06.522  - expressionStatement.expr:translate(name, "mingzi", String.class)
20:31:06.522  - stat:translate(age, "nianling", long.class);
20:31:06.523  - expressionStatement.expr:translate(age, "nianling", long.class)
20:31:06.523  - stat:add("height", Integer.class);
20:31:06.523  - expressionStatement.expr:add("height", Integer.class)


# experiment 

[INFO] Changes detected - recompiling the module!
[INFO] Compiling 24 source files to E:\javaProject\modelmaker\target\classes
20:40:42.718  - enter ModelTemplate
20:40:42.776  - birthPlace:"cn.replux.model",characteristics:{"Data", "Builder"}
20:40:42.777  - methodDecl.body:{
    translate(name, "name", String.class);
    translate(age, "age", long.class);
    add("height", Integer.class);
    reduce(grade);
}
20:40:42.777  - stat:translate(name, "name", String.class);
20:40:42.777  - stat:translate(age, "age", long.class);
20:40:42.777  - stat:add("height", Integer.class);
20:40:42.777  - stat:reduce(grade);
20:40:42.777  - birthPlace:null,characteristics:null
20:40:42.778  - methodDecl.body:{
    translate(name, "mingzi", String.class);
    translate(age, "nianling", long.class);
    add("height", Integer.class);
}
20:40:42.778  - stat:translate(name, "mingzi", String.class);
20:40:42.778  - stat:translate(age, "nianling", long.class);
20:40:42.778  - stat:add("height", Integer.class);
20:40:42.833  - enter ModelTemplate

# experiment

23:28:04.927 r - enter ModelTemplate
23:28:04.983 r - birthPlace:"cn.replux.model",characteristics:{"Data", "Builder"}
23:28:04.983 r - methodInvocation:translate(name, "name", String.class)
arg.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent
args:name
arg.getClass:class com.sun.tools.javac.tree.JCTree$JCLiteral
args:"name"
arg.getClass:class com.sun.tools.javac.tree.JCTree$JCFieldAccess
args:String.class
arg.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent
args:age
arg.getClass:class com.sun.tools.javac.tree.JCTree$JCLiteral
args:"age"
arg.getClass:class com.sun.tools.javac.tree.JCTree$JCFieldAccess
args:long.class


23:28:04.985 r - methodInvocation:add("height", Integer.class)
23:28:04.985 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCLiteral
23:28:04.985 r - meth:add,args:"height"
23:28:04.985 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCFieldAccess
23:28:04.985 r - meth:add,args:Integer.class
23:28:04.985 r - methodInvocation:reduce(grade)
23:28:04.985 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent
23:28:04.985 r - meth:reduce,args:grade
23:28:04.986 r - birthPlace:null,characteristics:null
23:28:04.986 r - methodInvocation:translate(name, "mingzi", String.class)
23:28:04.986 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent
23:28:04.986 r - meth:translate,args:name
23:28:04.986 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCLiteral
23:28:04.986 r - meth:translate,args:"mingzi"
23:28:04.986 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCFieldAccess
23:28:04.988 r - meth:translate,args:String.class
23:28:04.988 r - methodInvocation:translate(age, "nianling", long.class)
23:28:04.988 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent
23:28:04.988 r - meth:translate,args:age
23:28:04.988 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCLiteral
23:28:04.988 r - meth:translate,args:"nianling"
23:28:04.988 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCFieldAccess
23:28:04.988 r - meth:translate,args:long.class
23:28:04.988 r - methodInvocation:add("height", Integer.class)
23:28:04.988 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCLiteral
23:28:04.988 r - meth:add,args:"height"
23:28:04.988 r - meth.getClass:class com.sun.tools.javac.tree.JCTree$JCIdent,arg.getClass:class com.sun.tools.javac.tree.JCTree$JCFieldAccess
23:28:04.988 r - meth:add,args:Integer.class