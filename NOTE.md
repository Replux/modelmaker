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