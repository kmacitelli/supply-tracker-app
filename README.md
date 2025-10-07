# Supply Tracker App

To run this app locally, first make a file `response.txt` with the content

```json
{
"weight": 10.0
}
```

then host a python server from that directory using the command `python3 -m http.server 8080`

You should be able to access the file at the URL `http://localhost:8080/response.txt`
