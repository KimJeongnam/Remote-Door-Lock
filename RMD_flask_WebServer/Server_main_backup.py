from flask import Flask, request, jsonify, session, redirect, render_template, url_for
import sys, time
from BLEDEVICE import BLEDevice
from my_database import MyDB

app = Flask(__name__)
app.secret_key = 'any random string'


try:
    hm10 = BLEDevice("D4:36:39:D8:DA:29")
except:
    print("bluetooth connect Fail")
    sys.exit()


@app.route('/')
def Login():
    if "userid" in session:
        return redirect(url_for('check_session'))
    return render_template('Login.html')

@app.route('/index')
def indexhtml():
    if "userid" in session:
        userid = session["userid"]
        return render_template('index.html', userid=userid)
    return render_template('result.html', status="No Session!")

@app.route('/Insert_Member_submit')
def Insert_Member_submit():
    if "userid" in session:
        id = session['userid']
        if id == 'admin':
            return render_template('insert_member_submit.html')
    return render_template('result.html', status="permession denied!")

#---------------------세션 처리----------<<<
@app.route('/check_session')
def check_session():
    if "userid" in session:
        userid = session["userid"]
        if userid == 'admin':
            return render_template('admin_index.html',userid = userid)
        else:
            return render_template('index.html', userid = userid)
    return render_template('result.html', status="No Session!")

@app.route('/Check', methods=['Get','POST'])
def Check():
    if request.method == 'POST':
        id = request.form['id']
        passwd = request.form['passwd']
        db = MyDB()
        result = db.select_Member(id,passwd)
        del db
        if result == True:
            msg = "Success"
            session['userid'] = id
            return redirect(url_for('check_session'))
        else:
            msg = "Fail"
            return render_template('result.html',status = msg)
    else:
        return redirect(url_for('Login'))
    
@app.route('/session_out')
def session_out():
    if "userid" in session:
        session.pop("userid",None)
    return redirect(url_for('Login'))
#------------------------------------------->>>

#----------------- Member 관련... ##-----------------<<<
#Member list 출력
@app.route('/Member_list')
def Member_list():
    if "userid" in session:
        id = session['userid']
        if id == 'admin':
            db = MyDB()
            rows = db.select_MemberAll()
            del db
            if rows != False:
                return render_template('Member_list.html', rows = rows)
            else:
                 return render_template('result.html', status="Error!")
    return render_template('result.html', status="permession denied!")

#Member 입력
@app.route('/Insert_Member', methods=['POST'])
def insert_Member():
    if "userid" in session:
        id = session['userid']
        if id == 'admin':
            if request.method=='POST':
                id = request.form['id']
                passwd = request.form['passwd']
                name = request.form['name']
                db = MyDB()
                if name != "":
                    result = db.insert_Member(id,passwd,name)
                    del db
                else:
                    result = db.insert_Member(id,passwd)
                    del db
                
                if result == True:
                    return render_template("result.html", status = "Member Insert Success!")
                else:
                    return render_template("result.html", status = "Fail")
            else:
                return redierct(url_for('Member_list'))
    return render_template('result.html', status="permession denied!")

#Member 삭제 
@app.route('/Member_delete', methods=['POST'])
def Member_delete():
    if "userid" in session:
        id = session['userid']
        if id == 'admin':
            if request.method=='POST':
                delid = request.form['id']
                db = MyDB()
                result =db.delete_Member(delid)
                del db
                if result == True:
                    return render_template("result.html", status = "Member Delete Success!")
                else:
                    return render_template("result.html", status = "Fail")
            else:
                return redierct(url_for('Member_list'))
    return render_template('result.html', status="permession denied!")

#----------------------------------------------->>>


#------------------- Log 출력 ##------------------<<<

@app.route('/Logs_List')
def Logs_List():
    if "userid" in session:
        id = session['userid']
        if id == 'admin':
            db = MyDB()
            result = db.select_Logs()
            if result != False:
                return render_template('Log_list.html',rows = result)
            else:
                return render_template('result.html', status = "Fail")
    return render_template('result.html', status="permession denied!")
#--------------------------------------------->>>

        
#-------------------------도어락 컨트롤----------------<<<
@app.route('/controllDoor')
def controllDoor():
    if "userid" in session:
        buf = request.url
        buf_list = buf.split('/')
        url = buf_list[0]+buf_list[1]+"//"+buf_list[2]+":8080"
        return render_template('controllDoor.html',url = url)
    else:
        return render_template('result.html', status="No Session!")

@app.route('/api/v1.0/remote/65%5*6813%^$')
def DoorSwitch():
    if "userid" in session:
        id = session['userid']
        try:
            message = "DoorSwitch"+"\n"
            vh=hm10.getvaluehandle("ffe1")
            hm10.writecmd(vh, message.encode("ascii"))
            status = 'Success'
        except:
            status = 'Fail'
            print("send fail!")
        if status == 'Success':
            db = MyDB()
            result = db.insert_Ctrl_Door(id)
            del db
            if result == True:
                return render_template('result.html',status="Success")
            else:
                return render_template('result.html',status="Fail")
    else:
        return render_template('result.html', status="No Session!")
#------------------------------------------------------>>>


#-----------------------안드로이드 통신 REST FUL---<<<<

@app.route('/api/v1.0/tasks/<task_id>/<task_password>', methods=['GET'])
def get_task(task_id, task_password):
    if request.method=='GET':
        db=MyDB()
        result = db.select_Member_return(task_id, task_password)
        del db
        if result:
            data = {'done':True,'id':result[0][0], 'password':result[0][1], 'name':result[0][2]}
            return jsonify(data)
    data = {'done':False,'id':'','password':'','name':''}
    return data

@app.route('/api/v1.0/tasks/login', methods=['POST'])
def get_Post_task():
    if request.method == "POST":
        id = request.json['id']
        password = request.json['password']
                                                            
        db = MyDB()
        result = db.select_Member_return(id, password)
        del db

        if result:
            data = {'done':True,'id':result[0][0], 'password':result[0][1], 'name':result[0][2]}
            return jsonify(data)
    
    data = {'done':False,'id':'','password':'','name':''}
    
    return jsonify(data)
                                                                                 



#---------------------------------------->>>>>>>
if __name__=='__main__':
    app.run(host="0.0.0.0",debug=True)
