function findUniqueDigits() {
    const phones = db.phones.find().toArray();
    const uniqueDigitPhones = [];

    phones.forEach(phone => {
        const number = phone.display.split("-")[1]; 
        const digitSet = new Set(number); 

        if (digitSet.size === number.length) {
            uniqueDigitPhones.push(phone.display);
        }
    });

    return uniqueDigitPhones;
}

const uniqueDigitsFound = findUniqueDigits();
print(uniqueDigitsFound);
