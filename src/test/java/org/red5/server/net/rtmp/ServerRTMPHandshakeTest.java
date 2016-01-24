package org.red5.server.net.rtmp;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.red5.io.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for server side handshaking.
 * Test command for using rtmpdump 
 * <pre>
 * ./rtmpdump -V -z -r "rtmp://localhost/live" -a "live" -W "http://localhost:5080/demos/publisher.swf" -p "http://localhost:5080/live" -y "test" -v -o t.flv
 * ./rtmpdump -V -z -r "rtmpe://localhost/live" -a "live" -W "http://localhost:5080/demos/publisher.swf" -p "http://localhost:5080/live" -y "test" -v -o t.flv
 * </pre>
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class ServerRTMPHandshakeTest {

    private Logger log = LoggerFactory.getLogger(ServerRTMPHandshakeTest.class);

    // Red5 client generated C1
    // Time and version: 0000000580000702
    // Algorithm: 0 
    // Offset: 460
    // Digest: a143cbbc7b8b4838496f2667402c1bd742e6c73d051e94a284519d8d7966c6c6
    private String red5clientC1 = "0000000580000702dc0578676cf5adfbf9be471bd4903991ae3d34421109e4d6612e5613100c31e9d3842743689dd57b5f675dd8af1081fbc24c266873044fa372045a1a32e9f08cc974ec3b1152f898893fb7b91ee9d45a8a84c7125e77447b9273e9caff90b240a59985355a30ac6650a3363baf6e34aeb6fc78ecc8a7f746711980e4d83d45248191eb9458f52338df757d7fb60c9e876c965c5907da9f8956bc0afa21698bc92d67962bc65e6d5c36383077cad969e1c336519659396c1d9949a4f1c4279a02a9f4f26d821beb92a86ff4bc463cb2d749d14b545e50dc052a248426af96b7ccae15321d0d4cec0d5d5c6543c99e7cd37e42d71098117f6a09c3f7a351570acf26aadba70f9383f3d1ff789fb63494f956fac4fbf26f5a4acb9f8131211c0c2cb068cfc7d2b5ff6e53730fcfb5ce483bbb76587136877d7e1c757711174511751d819d4fc2cca78f878ea6d830bf833b098e77ef95547f3037cdb964318c7767f60e61acf3039d63e5dd6b86d7cee4569189a9f324ffebff70e05e1ff0c14dbbf35b865c9beb03f837e926cc1d41d9811610ef5e59b0e2afabc36a5bd89986b385f2267f9358d7291fcab51cb8e8de5b52e0bcd392fc655ce1e6ee9ff38608504d80e5aaa143cbbc7b8b4838496f2667402c1bd742e6c73d051e94a284519d8d7966c6c612a0c2814e03344821dafeb196df6cf9f130a2718e9b88af7bc57e4410e72a38014017aa7a12fa53312ed4cc53f2bc73c5e450b1dba68d380e5bd0ded42165e512a13ad401c25add1b658ba1303b7a6bf48b9abfd2121cce20b7f02f618c7ae5d1c62c3826c64ee66b81156c2c4e8d8ccfa01e6352709c27cf259c1edd3993af730c8ed9027c0136efdf135e3646bebe8a4bd8b44c32eb71bc2d7473a4bfb3454bf5cc1801b0a5883bbcc724b42fbb83bdba881a455c23108bdc12e6c4c7afa2568d0d8161b4ac1126c5bd96fd4291f6e40be4e5d780a01067238b578073ec3bb0596c7ab6c7593c4bc1a79edfee22fae0c342d83010d2c77b43e8f4ce8d1c331dd97e1a69cbeb5b894316f519b3648b75d7d94091f6b9db7a4a7efcd98d606f819433e7344ad729846f04ffe2ca6a3bca43bb69d08a30cc189f1da97f69749868bf7302a7ceca8521ce673361a7918cec36218ed6cc47ad8e9d7b56d337d5d97f6828dfa78977e80e3236be323a4b4d4ac8c5a222f4675a1a44624a9ca30867193a8552e948585f38ac93b3e70f5b123a4149cf454effe17cef78aed52d70f5abcaba8e7bbdf0b452901a0a853a8ba173f5e895646ddf4a7b27b82e2b4052c3507d72563c123f9e55896fbf030c247591ab084db9a6a11d5e7bda60820f034f45f2b5825fd25963fabd93fecea0b07f959550bc12e1ac4d746e5c91704e57946e0724c7ec1f747e4205a7db483adafe62d9616551b3b6368c74ddd2c1afca81d068957a3f3ac219ef2a0c3d4469647b41a9633eead2d9647dfef334f138045a15b1920c8754f829084421d68c08940b5eecea6a68d29bc6a096c6d7b564a2e10b1a3eba48bc5d205913feab5c078b8c4877fecde82f7cb9510dfc297215798c25bd9207da4faed4f07dea78e40363ce75d439269cb3e61a71b5d4fec457ab58f8631b7ce7444454a10f7f67c9b5109ebc9c50c3491ea328e7b07e52f9f44aa9bfe88224f13e44b9838fa10aa4285a2fd95f74cac9271c851a4df78294d5673dd826a064cab5c50971a4cf2b81c669a8ec1c5f6d8acd1725796f0b7cca33b953457bdfb719ac31648b8969056232c877fa4a046e0ef1ba45f31149d5089e4a262cd52b18dcb5516bb2d2eec2baf2c6ac6b712ac76946f2ca7fcff3286bc9d62c5756b9e5e14ec9f90f415fadf3eba5c18a506f58c6929a8f96804efb4634baffb58353544b81f3a18ca7313ee8aafd25099a7802e23a0242b4e7cb61a098a748d705119513dc5e6459df926f6b7f385f7a3953b68f219e60da6df839b32e88e974f98e2d85b9615469ccde04c06624c4d62c439933d60bcc0fef2e286ebf3165e5cf1530bfd58097f271853220c6e7affaec85844613bb2cda966ac6f486dca0a9bf49523535a249f116ce0038f5e2872566c9d6d88cd81bcaccc7631be78248e3c44832305b4655a47a2a01a79848177c8059c3";

    // Chrome version 47.0.2526.111 (64-bit) FP version: 20.0.0.267
    private String chromeFlashPlayerC1 = "0000068e8000070282a38d183ee941036bd4fdf6b99369ee3f9b2af7f271a28c135329e29bde85bf67fd607fc566160fb62aeb4f190b935c711a6acf1111fa8bea7c677f78b84e2b6e373a931414727350535544221361fc9180aa71bacbcdf94ef6019038c03e06da0a0069d9e1cd7fcd275ae5f389683a835471fd0fb47cfde79af2bafefe3865a1e5cdd8b8a118a742f48ed1326ff84249de2f9f319f9efd672fde65a0ada9a3e57305d30efdb5cecb6924d5a7c8b36cf9a60c53be6c53077d2cda7ac3eeed8c29b40e0b3e06f0bfe7f167aa1c37f7f774b4c7dbb8721b76c6a22fe9c12640b824a8c88caaa5b2b0df3555504fb180b450a4c2b67570873de12c5790e501c05584fc3232103ce8e33ecc294b32356ec0d803903deeac831e5b7bae67626c8f403b664f9ab4abd936c31be19fcdac191faaea6dc177a9513fdddfedf43678663e149a6ca8abf57cc39049436bd14e5bf16f385733c053186b69a096acb0e2f8db7ed5e67cbfc9ab91165fb3b7911c7f6f97c8f667dbe6588fd93fbd77b418fd57557b9adba503c17f23237d84fde0d1458d4295de23bec4da78fd2722f3746213aa6f9a4a8159c0545129aeecd12803b419e31d49b9bb2183a46e90e5231c65c2b8b53ada899f3a271cdf37690c166b1719374ebaddd727e95d94df74e02bb375dc194a92264965c4a7a94b5e26b4fb1e7dfc488fd1156c83906cda5ce20dabefbaf8085c92a45cd373e856ee1baed027068c4f3d3694c2c2f31d85b445b416bc97222fa7fd9f5ee43d583d360c5223de8894beda51c9ae457fa5e6af9fb12c97927b3123da3bec972bfccedc5b811931fb7fd57d4392ab5b7776833f0b0e9e80138b161e62f65daec5abbec03e48572f8185b787c19b91eacdc0c12dff4d0fb0a0a0cfe78581ffcf32159b2e949ad40f6eee574233f5c1faa955e3f6f0ac232c0b4c6443b8d5e48f0531cdcaae2ff52d4d3def1bf174f12bd093057dfe9170ca6e717d2f3043c998d3a20ed5ddb80815510647aa06c672ba45bdd9ee282b337ceef0c2eb9867ee1d65bb251d0695afc1778ed258953af0b1752f78babc2a0643f7df068b5dea9344d667facf4ab9e9073cf235260bbbe23618118e7b1fedabd106c7e9c786b780ca22254694f3614374a840dcdfa32bdf44d1ec1f0128ebb8d988d350bc75e286fbf9142d19c4e2f450dcadfacaa0c380c6e3c34cd61be34bf83cf793ab0f84fe2ab6c9d6a4676decab9c6aac0e3ae7da6808b63b8cd048b44c9f7d432675632d7a553a95a47126e987e2d53d948672d12e8a2953d7cae8611690907b1f32e98f754c29ac7c2b3fecb8e1f70bc71a38a37a82e18909c52476e34d3eb3e7d5f03035b76b3af6e50c8f7ba58ec9b29d0b34d538502ecc57f1093044654df78768e6edef7627f700b4724ac2e4d0ae3d1ef87364e50d88bda26808b4e5ae4ec9732d5deb29f0d367173e1be0a3a2c94cec282a8c636fd1fa179f23da3ad2555611c182ef63e8bc0568c24dda5f1fe26e6ab955626cd2ee4e56ae0a7e1b78cbad84116454a96f116ce6a83241a3a4f4b38168efea45c8c80c67829260aab28805c8e57b2ab37fafa104be04568c36f4d56493aed0c96eb5c5635b67874c0bb406ed780d6eb20217388fbf38960064162fc429c9aaa43a7f87a4beba8e6e1a6d9d5c4e1a40223d7f917e9762594fb56e279c693b7aa6dc2bdebec8bcf47928850d3acaf551d6e2b39b522f77ddcc69f87622539fe37fd922f15a4530e8d36bfbf5d59245e411e223b8a1e2474bce6976c75c020a948fe857375c474a3e470fde2f90756f62cd25a094c320544be6448a2b50743b1b4f4a9e03160927a6a327427ff9da1cd7303fc041cadfed03a0cfe748b64a64041db58e3329ec76765b0282afb5fc93d19c6f72d4106bb32fd94747ba567381728b89456c7498eb8281c9f1c50df6b41574e4a375a1562233cc44eb6d5d36f8380a5a6fa035403c07bd5a5a1260ebb50225162310fd2c813931870f29beb9b8c161ec8ace4478979a34a53f1fa4be9674dcfea0b5747022d86e936ec045a4c4bd80c75fd3e60702d54222ac8d529ee9fdf8b0b0ca4fd2f987430a9741db0f2c0073656216fe0d3385801ac1e9721dc74cc1858e5c95b36c9f57a91578beef80";

    // Firefox version: 43.0.4 FP version: 11.2.202.559
    private String firefoxFlashPlayerC1 = "000006888000070276516e1353d7e9d3bdf8aed918f03ecb0b83a50b9ea618823fb76e86c29b8976a2188673150d438cfc9340d75011594687380a44429bd536ce2f9f10d718067d387f13e82ace4b2730849c4f3c94be7e42a55517aecf836a131b65cedba2d89b7cb83983cbf840f029aa8988d012daf2edd78146cab63d43a094fa65dea5a5aef719146b317c22af43cafa41a58963f3dad4edb3361ebc7bee2efc2f3367ffcd48f02d79e7dbb5e3025881c787f7cd2cf74ccbdc52ff44c4feea9a3786340020de94dd25f341f37e76726a3578426beaffc7dbe747b99995608e971461dcd6b2a695a90501c9cf8582317453075045441c0205ec46c4386ea946c23de5457147b8733809c9f25fb716c90b2696078c7450f5c576a3be763f4b2a2815f998f1d9a68a5b1a63ad544526fdd90d04df0ffd116fbf73dc054f67fcad5e95b42687d72be33c132c67ce79015c5ba134504d77ba2ced3c24d271468dda43a51ebdeb386c5d492b7c6b2e5e0fe135c77db709e988a8c31999442a387146c7718d71deb6bc0ddd61edbf9643511fbc0cd83fe94df3b9090e858642530f8d61e996e6208a6f7e1628547a9d3319c51a39b181f144667365a07c009b7710520875d5297372d1bf3a32fe5281cd4efd42a9b17c07cfa3b68056b8064412cd7d5fb733020233ab20d3ca8a9b40f6a3647aeabb8e8bd76044cb190d78fb52098dde1bcd9b6ff12c50cfa8a802505005832d95196fcba0a0c1ae478040766d9bb97a34fa1aab3cd8dcaf9fed674801041744112c9de3f0e4fca3eb82a38614d259748fe4688ecbd57896dbbd677714e194650224dd6b14d66a5458f292ae5e1e370e682715b6e7b6ffb4d9200d8ff58513acc585e37d15ad3302da406caafa8f791c6c29facb933cc0c3646f8675208c47510319cc0ba77071611ac9104ca0e2249db77e0232aa57dab2e6f99037487b0c1a26447aa682654ad255bf14eb16408e4882651b610e58edc9168459d91d1b5c900ff15515ad5c5249a22bdf2a1abe15dddbe53beab93762662edeee18d607e6848ad63d348b09abf5420081a2b912ac96adb786ac3304dbeddcdf56cc0751dd3e45ceaea115c33418160885d816d6a1e6f5fab248c6be60ad9cfb96e2ba4066fa5a880742bd922781e0f72e48ba53cbcb0e3aa2b322f38734dd43671861da95c57b3b41f10feccbfd4d57239577b90dfe6a464c2928d679a08b86f4adb87a088d8574763452cf5b6608ea37502d675d013895826b5697fc914de21d7d23c8b1ec752915b251a2bb1f52c3a9d161ebb6450c24a4afd113c9a690676602fe3bcab498d134735a096149b70cb78bfa153691f1edc65c23938ff6b77989e8392470897082f4168e98668455a31effd10d53fed0bd7b2d6c0fd24cf70e7783f635f4086c6a463aba6e63b16563334efafcb1569d5f77e337d6aee6900f2e9edd43fa98cd666342d3d9e5cc949eb37ed12d20909a5d1c5945bebd39ff7772a38d9b9bccf544eaef67a8a6f2fbd622c42b098e37876d8accc710806b4132c61632405a560adfc82cff1bd9cd0d4baf2bd18a054b503acb99ff396207d3c625ce912eeebf88cca68a19ff10b1c5c2341dc11df1155f018ce000a91c9a911c7e2a919c0a19d023de5107c40a64bb8529f4f457b471d439a6a73dbc1c9d8190ceca5aa3e1634445f82efdeceae3c305b848c921a71de58a7b887f78736e0ad51cb1b39d4c137bc6d037c4fb663facbf53376052bfb52441fd9e2e90f663370864ac0e7a9635d7d62ef0a09cf1e768ee9102fc31180ff193db4a29928ce5249010a36d6dee26d783d0dc8bccd472a8a7ef88d6626f7816d5ddb65e7ba266939bfe47343f5007792bf20308e0040a4e2696ae8f064326dba9ce18aed06b082735d5429275d34830b1df46366c2cf363ab6e05e94bd3a15c70251f3af3d2f4d85dd99bd3f26708c365b296ba85e3ba0cf9688b597636058012013b3686e092814f237d3b7f74a7a0024ffd66580825810df1106fe013f503c4677c35e377dc0ee97de324a11bd3c1bd829119bf86ca5b47b17ad268735ac59db02584ccbce702eebb464f10c2c6f921de68a43c83491b94d4201b7bbca037c6b70ecb3ed7737feb7484a9a9fa0d02dd6594238486c2371112e1f9";

    // FFMpeg version: 2.4.8 with libRTMP
    private String ffmpegC1 = "366778720000000067458b6bc6237b3269983c647348336651dcb074ff5c49194a94e82aec585562291f8e23cd7ce846ba581b3dabd77e50f241b12efb1eb741e3a9e27946e145757c005f51c262d05b54082012f827b14d1b231602e8e9161fe7cd90118d43ef66760f0e145a2552332ef99c106372ed0d33c2dc7f9fd7ef1bc9c4a7419a07686b66fb6a4e325de4250d509b51b7d71b4331ba2d3f58e4837ca33071255ad9bb6225616c435d898c6205b13a3317a31d7258a84324e95a1d2d5e846367d4a8a275abbded08b28c8379cdd05343c6e0030b9b769a18b49ee4545424f3711186a82c0ec43608821d900274f8953a4186130821f57f1e3dbd3d7cdc8d7b7387f0ea6c701a2222e9dd16453ec80630a1d44f6141c29a41e1f87755fcad0b44672307053e820438015f46777ec62477972a485ceab96324dc4a885e6bd3ea519677512d8fd70b5838a43e155c5855382a4ea670ec42236ab07c482a3bd44e1dfb065a72329ad82cafcce4573c8d6d7a548f584bec892254181be96ddb7f43385ca4447602f9ff321a484a68fe78945743bb9a74fb40c23dfa26a01baadea1793ac3c675fb85e61229a5c670d1ed0e52e63f4a3705f04e4f3cc1f9237cb79b6494c75a2775653839d80ff11cbe15011861a85b23898c3947f9e94f355cafb515bb261274a8b6340d993c23100fb66a3f95405761b1570c7eeb35ae77f1e49b57b3500c31057ef85fef5d302ff70ba72500bfba1de984d04aa1ea481f3a828113e50ab75dca8f0f100b709065cb4a0115d07f5e5f48318a0947029d796447b906bd96c2421f128e16235dba1e1e3f1e66a89ec75d1c470a547beed37b64c5d951c5fd3e61142bf70b737b44115a3e9642c582030a5eb1f2084b23321a79d30f3b632feb683b81624970dfb66064eea5062406331411caff7f9e70271a0911ea71dc590f10aae0b77fd45beb06acd96d6ff21142091b5e880010212776afa8044c3b701617337ee114cde72232e30ede7450c5eb6848d6f62d47d4b74615c32a4a5c01ee39bb4ffc576f01c10c2284f1431901ef60ba24f3269b57017f7d30da49f5a555700b37b85fe11e80501aac88041c01b85f7f8fa76a23bd7276f85ac76f29705f6af8185e7da434355f1b82a1731377e67db5555c55ca2aa63f4ee7fc14e8d33d6a9812c97132f6da0938992953e0e8bf1f79ca92504d5c541d3deaad59341a8f28bc5d152a5f6e9f1d4e1b7e0977820851fac5a01ccb4b58536c285e4105fd587cac6ad82386d4e64521fe105c2bfa7f0eaa91593c1a59d84b556adf78a2aab739be8d0d2b70ec806cb5219e3773e369003b17272c04099b4c5cb7a76ad329f01d36ff75569450d13db312b03dafc90827e2ac255bf0fc5d17e4e3974f9e0a3b054f6bfd3432ff1559158d435649319e51fd4a6e2c82b5a1174e2ef74da9b54650088a885d702c082ad4afc65eb21be2198a85e075291aa65754c699534813ee209a0627440ae8370bbcf65721d51d4e700ef1d25718aeff0ba8473e0e44f0482eacfed0495b5aee4bf3b951558eabf6244c574c63d79de9242db6312a9bc24918099dff7d42437500e5f3e76906e86d2ac4f816183322df37af9db47acd829f75a34ee761844d7b597f9e810f2dd4c757ad672131d4641b6376e7b578476e4875de4c536e32de0d1a1c8c9665ec3d26464a8c0d26c4d3d473302e6f74f68ade6f202ec33f23e8c0498536d5146c850f23fb85aa6eb2ec063f0748593b0423aa6cf42f7c3fec3b41250b0b1817b9289357205e205dbaa8cc1186ab324dc3ac073f3ef6476b054ab45cf180cf16ec5d691cd9aecf3f6768850f33ccb111b7fb222e9946932950584877a3394974e3d2a04f142c1d6bd367b868d95d7f3f345ae02af74f79325e945454a0dfef4df2d5232110815b13a8274909f6f8cd0d05b1d75294638a2e0104e624bed96a2ab4c1aa0bbcacb23644859d77786eb24afaa2fa2149cf515469ef8161e600643e237e2114d05707711acd1550da794442699e9a1a6a255e477eb38d364c713b6a7e517b32511b461f25cfba29b3ab5b5d486bbf5184630f7e538b4b2b3a41e37294e46a11fbb29434313ab1009995426490161f63323e9725576fad0e44d8c96eeeea495c9bf44a06bc467c39";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testServerDigest() throws InterruptedException {
        log.info("\ntestServerDigest");
        InboundHandshake in = new InboundHandshake();
        int algorithm = 0;
        byte[] handshakeBytes = in.getHandshakeBytes();
        // get the handshake digest
        int digestPos = in.getDigestOffset(algorithm, handshakeBytes, 0);
        log.debug("Digest position offset: {}", digestPos);
        in.calculateDigest(digestPos, handshakeBytes, 0, RTMPHandshake.GENUINE_FMS_KEY, 36, handshakeBytes, digestPos);
        log.debug("Calculated digest: {}", Hex.encodeHexString(Arrays.copyOfRange(handshakeBytes, digestPos, digestPos + 32)));
        Assert.assertTrue(in.verifyDigest(digestPos, handshakeBytes, RTMPHandshake.GENUINE_FMS_KEY, 36));
    }

    /** Serverside test */
    @Test
    public void testInboundHandshake() {
        log.info("\ntestInboundHandshake");
        IoBuffer r5c1 = IoBuffer.allocate(1536);
        r5c1.put(IOUtils.hexStringToByteArray(red5clientC1));
        r5c1.flip();
        InboundHandshake in = new InboundHandshake();
        // try old method
        IoBuffer S1 = in.doHandshake(r5c1);
        log.debug("S1: {}", Hex.encodeHexString(S1.array()));
        Assert.assertNotNull(S1);
        // reinstance in, prevent corrupt test
        in = new InboundHandshake();
        // send in the first part of client handshake
        S1 = in.decodeClientRequest1(r5c1);
        log.debug("S1: {}", Hex.encodeHexString(S1.array()));
        Assert.assertNotNull(S1);
        // pass C2 to in
        //IoBuffer S2 = in.decodeClientRequest2(r5c2);
        //Assert.assertNotNull(S2);
    }

    @Test
    public void testValidateFromBrowsers() {
        log.info("\ntestValidateFromBrowsers");
        // no handshake type bytes are included here, the C0
        // server side handshake handler
        InboundHandshake in = new InboundHandshake();
        // CHROME
        IoBuffer cc = IoBuffer.allocate(1536);
        cc.put(IOUtils.hexStringToByteArray(chromeFlashPlayerC1));
        cc.flip();
        log.debug("Validate chrome: {}", cc);
        boolean chrome = in.validate(cc.array());
        cc.clear();
        // FIREFOX
        cc.put(IOUtils.hexStringToByteArray(firefoxFlashPlayerC1));
        cc.flip();
        log.debug("Validate firefox: {}", cc);
        boolean firefox = in.validate(cc.array());
        cc.clear();
        // FFMPEG
        cc.put(IOUtils.hexStringToByteArray(ffmpegC1));
        cc.flip();
        log.debug("Validate ffmpeg: {}", cc);
        boolean ffmpeg = in.validate(cc.array());
        Assert.assertTrue(chrome && firefox && !ffmpeg);
    }


    @Test
    public void testOutgoingPublicKey() {
        log.info("\ntestOutgoingPublicKey");
        byte[] sharedSecret = IOUtils.hexStringToByteArray("04cde275ff2d72113cfac0e914bf4dab3bc747dfb63c23314b470181e7260a1f37ae3ef259f3bd3fe80ec5ebf99d501e4cce69d224268e6d5304cbfb94bc71d59f15564f96a089f9a93b5e08d9ea0c45ca5934ff2c9729cc73856fd130cb6bfe29f14a0ec36e0eee0cd5c21c1d08f6f9979adc162d24831318a3b9145d835222");
        byte[] outgoingPublicKey = IOUtils.hexStringToByteArray("d5055cd576014c41fc91811a7f6aaaacc8f5bef9383cabb3c91afd392448255ef14a38e8c985197652a47a31e2852d7923dae7c2c10df3b325556c4f2fbc14b04e244570c42526e67d2c5c3e75fcd1732c7b915839653274df15d887c10852dae81d54e52fe26946fd7936fc69926e7a33c9e3aba7ae2a93cbbd4c481cde3f90");
        InboundHandshake in = new InboundHandshake();
        byte[] rc4keyOut = new byte[32];
        in.calculateHMAC_SHA256(outgoingPublicKey, 0, outgoingPublicKey.length, sharedSecret, RTMPHandshake.KEY_LENGTH, rc4keyOut, 0);
        log.debug("rc4keyOut: {}", rc4keyOut);
        Assert.assertNotEquals(IOUtils.hexStringToByteArray("0000000000000000000000000000000000000000000000000000000000000000"), rc4keyOut);
    }

}
