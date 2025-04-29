from flask import Flask, render_template, request, redirect, url_for, session, make_response, jsonify
import psycopg2

app = Flask(__name__)
app.secret_key = "rahasia"

# Fungsi koneksi ke PostgreSQL
def get_connection():
    return psycopg2.connect(
        host="localhost",
        database="DBTwitter2",
        user="postgres",
        password="0000"
    )

# Halaman login
@app.route('/')
def index():
    return render_template('login.html')

# Proses login
@app.route('/login', methods=['POST'])
def login():
    username = request.form.get("username")
    password = request.form.get("password")

    conn = get_connection()
    cur = conn.cursor()

    cur.execute("SELECT id, username FROM users WHERE username = %s AND password = %s", (username, password))
    user = cur.fetchone()

    cur.close()
    conn.close()

    if user:
        session['user_id'] = user[0]
        session['username'] = user[1]

        # Simpan juga cookie (opsional)
        resp = make_response(redirect(url_for('home')))
        resp.set_cookie("username", user[1], max_age=60*60*24)  # simpan selama 1 hari
        return resp
    else:
        return render_template('login.html', error="Login gagal! Username atau password salah.")

# Halaman home
@app.route('/home', methods=['GET', 'POST'])
def home():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    user_id = session['user_id']
    username = session.get('username')

    conn = get_connection()
    cur = conn.cursor()

    try:
        # POST: tambah tweet atau komentar
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

        # Ambil tweet
        cur.execute("""
            SELECT tweets.id, users.username, tweets.content, tweets.created_at
            FROM tweets
            JOIN users ON tweets.user_id = users.id
            WHERE users.id = %s
            ORDER BY tweets.created_at DESC
        """, (user_id,))
        tweets = cur.fetchall()

        # Ambil komentar
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
    return render_template('home.html', tweets=tweets, comments=comments, username=username)

# Logout
@app.route('/logout')
def logout():
    session.clear()
    resp = make_response(redirect(url_for('index')))
    resp.set_cookie('username', '', expires=0)
    return resp

# Jalankan server Flask
if __name__ == '__main__':
    app.run(debug=True)
