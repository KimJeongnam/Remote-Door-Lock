import MySQLdb
import sys

#--coding:utf-8--

"""
db = MySQLdb.connect("localhost","root", "qwjdskap!2", "remotedoorlock")

cur = db.cursor()

cur.execute("select * from Admin")
while True:
    admin = cur.fetchone()
    if not admin: break
    print(admin)

"""
from sqlalchemy.dialects.oracle.zxjdbc import SQLException

class MyDB:
    def __init__(self):
        try:
            self.db = MySQLdb.connect("localhost","pi", "qwjdskap!2", "remotedoorlock")
            #db.row_factory = dict_factory
            self.cur = self.db.cursor()
        except:
            print("database connect fail")
            sys.exit()

    def select_Member(self,id, passwd):
        sql = "select * from Members where id='"+id+"' AND passwd=password('"+passwd+"')"
        try:
            self.cur.execute(sql)
            rows = self.cur.fetchall()
        except:
            print("admin sql execute error!")
            return False
        if len(rows):
            return True
        else:
            return False

    def select_Member_return(self,id, passwd):
        sql = "select * from Members where id='"+id+"' AND passwd=password('"+passwd+"')"
        try:
            self.cur.execute(sql)
            rows = self.cur.fetchall()
        except:
            print("admin sql execute error!")
            return False
        if len(rows):
            return rows
        else:
            return False

    def select_MemberAll(self):
        sql = "select id, name from Members where id<>'admin' ORDER BY id ASC"
        try:
            self.cur.execute(sql)
            rows = self.cur.fetchall()
        except:
            print("Members Select execute error!")
            return False
        return rows
    
    def delete_Member(self,id):
        sql = "delete from Members where id='"+id+"'"
        try:
            self.cur.execute(sql)
            self.db.commit()
        except:
            return False
        return True
            
    def insert_Member(self, id, passwd, name=None):
        if name==None:
            sql = "insert into Members values('"+id+"', password('"+passwd+"'),default)"
        else :
            sql = "insert into Members values('"+id+"', password('"+passwd+"'), '"+name+"')"
        try:
            self.cur.execute(sql)
            self.db.commit()
        except:
            return False
        return True
    
    def insert_Ctrl_Door(self,id):
        sql = "insert into Door_Ctrl_Logs values(default,'"+id+"')"
        try:
            self.cur.execute(sql)
            self.db.commit()
        except:
            return False
        return True
    
    def select_Logs(self):
        sql = "select * from Door_Ctrl_Logs ORDER BY ctrl_time DESC"
        try:
            self.cur.execute(sql)
            rows = self.cur.fetchall()
        except:
            print("Members Select execute error!")
            return False
        return rows
    
if __name__ == "__main__": 
    bb = MyDB()
