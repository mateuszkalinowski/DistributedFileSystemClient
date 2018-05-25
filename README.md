# DistributedFileSystemClient

Węzęł danych do rozproszonego systemu plików "DistributedFileSystem".

## Uruchomienie

java -jar distributedFileSystemClient.jar [port]

[port] - parametr opcjonalny, w przypadku jego braku serwer zostanie uruchomiony na domyślnym porcie 4444

## Działanie

Serwer przechowuje pliki z katalogu dfsDataNode[numer portu] umieszczonym w głównym folderze użytkownika, więc do poprawnego działania
wymagane są prawa do odczytu i zapisu w tej lokalizacji.
