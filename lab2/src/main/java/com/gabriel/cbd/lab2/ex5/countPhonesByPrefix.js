function countPhonesByPrefix() {
    return db.phones.aggregate([
        {
            $group: {
                _id: "$components.prefix",
                count: { $sum: 1 }
            }
        },
        {
            $sort: { count: -1 }
        }
    ]).toArray(); 
}

countPhonesByPrefix();
