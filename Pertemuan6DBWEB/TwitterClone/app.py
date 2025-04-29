from flask import Flask, render_template, request, redirect, url_for, session, make_response, jsonify
# import uuid
import psycopg2

app = Flask(__name__)
app.secret_key = "rahasia"  # jangan ada spasi atau karakter aneh

# Koneksi ke PostgreSQL
def get_connection():
    return  psycopg2.connect( 
        host="localhost",
        database="DatabaseTwitter",
        user="postgres",
        password="0000"
    )

# Halaman awal (login)
@app.route('/')
def index():
    return render_template('login.html')

# Proses login
@app.route('/login', methods=['POST', 'GET'])
def login():
    data = request.form
    username = data.get("username")
    password = data.get("password")

    conn = get_connection()
    cur = conn.cursor()

    cur.execute("SELECT * FROM users WHERE username = %s AND password = %s", (username, password))
    user = cur.fetchone()

    cur.close()
    conn.close()

    if user:
        user_id = user[0]
        resp = make_response(jsonify({"message": "Login successful"}))
        resp.set_cookie("user_id", str(user_id), max_age=60*60*24, httponly=False)  # cookie disimpan 1 hari
        return resp
    else:
        return jsonify({"message": "Login gabebgal"}), 401


# Halaman home
# Halaman home
@app.route('/home', methods=['GET', 'POST']) 
def home():
    user_id = request.cookies.get('user_id')
    
    if not user_id:
        return redirect(url_for('login'))

    conn = get_connection()
    cur = conn.cursor()

    try:
        # POST untuk tweet atau komentar
        if request.method == 'POST':
            if 'tweet_id' in request.form:
                tweet_id = request.form['tweet_id']
                comment_content = request.form['comment']
                cur.execute(
                    "INSERT INTO comments (tweet_id, user_id, content) VALUES (%s, %s, %s)",
                    (tweet_id, user_id, comment_content)
                )
            elif 'content' in request.form:
                content = request.form['content']
                cur.execute(
                    "INSERT INTO tweets (user_id, content) VALUES (%s, %s)",
                    (user_id, content)
                )

        conn.commit()

        # Ambil tweet dan komentar
        cur.execute(""" 
            SELECT tweets.id, users.username, tweets.content, tweets.created_at 
            FROM tweets
            JOIN users ON tweets.user_id = users.id 
            WHERE users.id = %s
            ORDER BY tweets.created_at DESC
        """, (user_id,))
        tweets = cur.fetchall()

        cur.execute("""
            SELECT comments.tweet_id, users.username, comments.content, comments.created_at 
            FROM comments
            JOIN users ON comments.user_id = users.id 
            WHERE comments.user_id = %s
            ORDER BY comments.created_at ASC
        """, (user_id,))
        comments = cur.fetchall()

    except Exception as e:
        cur.close()
        return f"Terjadi kesalahan: {e}"

    cur.close()
    return render_template('home.html', tweets=tweets, comments=comments)

# Logout
@app.route('/logout')
def logout():
    session.pop('user_id', None)
    resp = make_response(redirect('/login'))
    resp.set_cookie('session_id', '', expires=0)  # Hapus cookie
    return resp
# Jalankan aplikasinya
if __name__ == '__main__':
    app.run(debug=True)
