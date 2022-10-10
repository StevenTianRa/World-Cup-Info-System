#include <fstream>
#include <string>
#include <sstream>
#include <vector>

using namespace std;

int main(){
    vector<vector<int>> user_arr;
    ifstream fp("xxx/user_data.csv"); //定义声明一个ifstream对象，指定文件路径
    string line;
    getline(fp,line); //跳过列名，第一行不做处理
    while (getline(fp,line)){ //循环读取每行数据
        vector<int> data_line;
        string number;
        istringstream readstr(line); //string数据流化
        //将一行数据按'，'分割
        for(int j = 0;j < 11;j++){ //可根据数据的实际情况取循环获取
            getline(readstr,number,','); //循环读取数据
            data_line.push_back(atoi(number.c_str())); //字符串传int
        }
        user_arr.push_back(data_line); //插入到vector中
    }
    return 0;
}