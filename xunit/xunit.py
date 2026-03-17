# wasRunがテストの実行状況の保持、テスト実行の２つの責務を持っていたため分離
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def run(self):
        # Pluggable Selectorパターン
        #   テストケース名を示す属性を問い合わせ
        #   返ってきたオブジェクトを関数のように呼び出す
        self.setUp()
        method = getattr(self, self.name)
        method()
    
class WasRun(TestCase):
    def setUp(self):
        self.wasRun = None
        self.wasSetUp = 1
    def testMethod(self):
        self.wasRun = 1

class TestCaseTest(TestCase):
    def setUp(self):
        self.test = WasRun("testMethod")
    def testRunning(self):
        self.test.run()
        assert(self.test.wasRun)
    def testSetUp(self):
        self.test.run()
        assert(self.test.wasSetUp)

TestCaseTest("testRunning").run()
TestCaseTest("testSetUp").run()
