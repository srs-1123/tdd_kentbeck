class WasRun:
    def __init__(self, name):
        self.wasRun = None
        self.name = name
    def run(self):
        # テストケース名を示す属性を問い合わせ、返ってきたオブジェクトを関数のように呼び出す
        method = getattr(self, self.name)
        method()
    def testMethod(self):
        self.wasRun = 1

test = WasRun("testMethod")
print(test.wasRun)
test.run()
print(test.wasRun)
