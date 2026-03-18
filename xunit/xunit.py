# wasRunがテストの実行状況の保持、テスト実行の２つの責務を持っていたため分離
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def tearDown(self):
        pass
    def run(self):
        # Pluggable Selectorパターン
        #   テストケース名を示す属性を問い合わせ
        #   返ってきたオブジェクトを関数のように呼び出す
        self.setUp()
        method = getattr(self, self.name)
        method()
        self.tearDown()
    
class WasRun(TestCase):
    def setUp(self):
        self.log = "setUp "
    def testMethod(self):
        self.log = self.log + "testMethod "
    def tearDown(self):
        self.log = self.log + "tearDown "

class TestCaseTest(TestCase):
    def testTemplateMethod(self):
        test = WasRun("testMethod")
        test.run()
        assert("setUp testMethod tearDown " == test.log)

TestCaseTest("testTemplateMethod").run()
