# BudgetManager

Aplikacja Budget Managed umożliwia planowanie budżetu miesięcznego. 

Aplikacja przy pierwszym uruchomieniu pyta użytkownika (w formie dialogu) o podanie następujących danych: 
  - imię 
  - dochód miesięczny 
  - planowana kwota miesięczna oszczędności 
Dane te powinny zostać zapisane w pamięci urządzenia, nie można ich edytować w przyszłości. 
  
Aplikacja przy każdym kolejnym uruchomieniu wyświetla listę kosztów dodanych w bieżącym miesiącu. W górnej części ekranu jest widoczna statystyka.
W przypadku braku dodanych kosztów w bieżącym miesiącu, prezentowany jest stosowny komunikat.

Dodatkowo aplikacja analizuje, czy miesięczny budżet nie został przekroczony. Wynik analizy prezentowany jest nad listą kosztów w odpowiednim wisoku statystyki. 
Analiza obejmuje: 
- jaki procent budżetu pozostał niewykorzystany (uwzględniając planowane oszczędności) 
- procent przekroczenia budżetu (jeśli takie ma miejsce) 
- po wciskaniu w kartkę statystyki - użytkownik zostaje przeniesiony do ekranu, gdzie umieszczono procentowy udział wydatków w podziale na kategorie.

Dodawanie kosztów odbywa się na nowym oknie, które jest wyświetlane po kliknięciu przycisku "Dodaj koszt" na liście kosztów. 
Aplikacja umożliwia dodawanie kosztów w taki sposób, że każdy z nich posiada: 
- nazwę 
- kategorię
- okres (miesięczny/kwartalny/roczny) 
- kwotę
Lista kosztów zostaje zapisywana w pamięci urządzenia.
Lista odświeża się automatycznie po dodaniu nowego kosztu.
