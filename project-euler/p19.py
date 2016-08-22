
def is_leap_year(year):
    if year % 4 == 0:
        if year % 100 == 0:
            if year % 400 == 0:
                return True
            else:
                return False
        else:
            return True

def number_of_days(month, year):
    if month == 9 or month == 4 or month == 6 or month == 11:
        return 30
    if month == 2:
        if is_leap_year(year):
            return 29
        else:
            return 28
    return 31

sunday = 0
monday = 1
class Date:
    def __init__(self, weekday, day, month, year):
        self.weekday = weekday 
        self.day = day 
        self.month = month
        self.year = year

    def equals(self, date2):
        return self.day == date2.day and self.month == date2.month and self.year == date2.year

    def to_string(self):
        print "weekday:", self.weekday, "day:", self.day, "month:", self.month, "year:", self.year

    def check(self):
        return self.weekday == sunday and self.day == 1 and self.year > 1900

    def progress(self):
        self.weekday = (self.weekday + 1) % 7
        self.day += 1
        if self.day > number_of_days(self.month, self.year):
            self.day = 1
            if self.month == 12:
                self.month = 1
                self.year += 1
            else:
                self.month += 1
                
if __name__ == "__main__":
    d = Date(monday, 1, 1, 1900)
    count = 0
    while not d.equals(Date(None, 31, 12, 2000)):
        #print "current date:", d.to_string()
        if d.check():
            print "found date", d.to_string()
            count += 1
        d.progress()
    print "result is", count
